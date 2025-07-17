package com.vertiq.broker.integration.service.v1.service;

import com.vertiq.auth.v1.model.GenericResponse;
import com.vertiq.broker.integration.service.v1.entity.Holding;
import com.vertiq.broker.integration.service.v1.entity.HoldingSnapshot;
import com.vertiq.broker.integration.service.v1.repository.HoldingRepository;
import com.vertiq.broker.integration.service.v1.repository.HoldingSnapshotRepository;
import com.vertiq.broker.integration.service.v1.repository.UserBrokerAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UpstoxPortfolioService {

    private static final Logger logger = LoggerFactory.getLogger(UpstoxPortfolioService.class);

    @Autowired
    private UserBrokerAccountRepository userBrokerAccountRepository;
    @Autowired
    private HoldingRepository holdingRepository;
    @Autowired
    private HoldingSnapshotRepository holdingSnapshotRepository;

    private static Double roundTo4Decimals(Double value) {
        if (value == null) {
            return null;
        }
        return BigDecimal.valueOf(value).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public GenericResponse handleBrokerDataSync(String userId, String brokerId) {
        java.util.UUID userUUID = java.util.UUID.fromString(userId);
        var accountOpt = userBrokerAccountRepository.findByUserIdAndBrokerId(userUUID, brokerId);
        if (accountOpt.isEmpty()) {
            throw new RuntimeException("No broker account found for user");
        }
        var account = accountOpt.get();
        if (account.getAccessToken() == null || account.getAccessToken().isEmpty() || account.getExpiresAt() == null || account.getExpiresAt().isBefore(java.time.Instant.now())) {
            throw new RuntimeException("Broker access expired or missing");
        }
        // Set up Upstox SDK PortfolioApi with Bearer token
        io.swagger.client.api.PortfolioApi portfolioApi = new io.swagger.client.api.PortfolioApi();
        portfolioApi.getApiClient().addDefaultHeader("Authorization", "Bearer " + account.getAccessToken());
        String apiVersion = "2.0";
        com.upstox.api.GetHoldingsResponse holdingsResponse;
        try {
            holdingsResponse = portfolioApi.getHoldings(apiVersion);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch holdings from Upstox: " + e.getMessage(), e);
        }
        if (!com.upstox.api.GetHoldingsResponse.StatusEnum.SUCCESS.equals(holdingsResponse.getStatus())) {
            throw new RuntimeException("Upstox holdings API did not return success");
        }
        var today = java.time.LocalDate.now();
        // Convert enums to string
        for (var holding : holdingsResponse.getData()) {
            String isin = holding.getIsin();
            String exchange = holding.getExchange() != null ? holding.getExchange().getValue() : null;
            Double quantity = roundTo4Decimals(holding.getQuantity() != null ? holding.getQuantity().doubleValue() : 0.0);
            Double avgPrice = roundTo4Decimals(holding.getAveragePrice() != null ? holding.getAveragePrice().doubleValue() : null);
            // Parse holdingType from instrument_token (e.g., "NSE_EQ|INE528G01035" -> "EQ")
            String instrumentToken = holding.getInstrumentToken();
            String holdingType = null;
            if (instrumentToken != null && instrumentToken.contains("_")) {
                String[] parts = instrumentToken.split("_", 2);
                if (parts.length > 1 && parts[1].contains("|")) {
                    String[] subparts = parts[1].split("\\|", 2);
                    if (subparts.length > 0) {
                        holdingType = subparts[0];
                    }
                }
            }
            // fallback if parsing fails
            if (holdingType == null) {
                holdingType = "UNKNOWN";
            }
            // Upsert holding
            var holdingOpt = holdingRepository.findByPortfolioIdAndIsinAndExchange(account.getPortfolioId(), isin, exchange);
            Holding dbHolding = holdingOpt.orElseGet(() -> {
                Holding h = new Holding();
                h.setPortfolioId(account.getPortfolioId());
                h.setIsin(isin);
                h.setExchange(exchange);
                h.setTicker(holding.getTradingsymbol());
                h.setCreationDatetime(java.time.Instant.now());
                return h;
            });
            dbHolding.setQuantity(quantity);
            dbHolding.setAvgPrice(avgPrice);
            dbHolding.setHoldingType(holdingType);
            dbHolding.setUpdationDatetime(java.time.Instant.now());
            dbHolding = holdingRepository.save(dbHolding);
            // Upsert or update snapshot
            var snapshotOpt = holdingSnapshotRepository.findByHoldingIdAndDatePart(dbHolding.getHoldingId(), today);
            HoldingSnapshot snapshot = snapshotOpt.orElseGet(HoldingSnapshot::new);
            snapshot.setHoldingId(dbHolding.getHoldingId());
            snapshot.setDatePart(today);
            Double dayChange = holding.getDayChange() != null ? holding.getDayChange().doubleValue() : null;
            Double dayChangePct = holding.getDayChangePercentage() != null ? holding.getDayChangePercentage().doubleValue() : null;
            Double lastPrice = holding.getLastPrice() != null ? holding.getLastPrice().doubleValue() : null;
            Double closePrice = holding.getClosePrice() != null ? holding.getClosePrice().doubleValue() : null;
            // If dayChange is 0, but dayChangePct is not 0, recalculate dayChange
            if (dayChange != null && dayChange == 0.0 && dayChangePct != null && dayChangePct != 0.0) {
                if (lastPrice != null && closePrice != null) {
                    dayChange = roundTo4Decimals(lastPrice - closePrice);
                }
            }
            snapshot.setDayChange(roundTo4Decimals(dayChange));
            snapshot.setDayChangePct(roundTo4Decimals(dayChangePct));
            snapshot.setTotalPnl(roundTo4Decimals(holding.getPnl() != null ? holding.getPnl().doubleValue() : null));
            Double marketPrice = roundTo4Decimals(holding.getLastPrice() != null ? holding.getLastPrice().doubleValue() : null);
            snapshot.setMarketPrice(marketPrice);
            snapshot.setCreationDatetime(java.time.Instant.now());
            snapshot.setUpdationDatetime(java.time.Instant.now());
            // Calculate investedValue and currentValue
            Double investedValue = (quantity != null && avgPrice != null) ? roundTo4Decimals(quantity * avgPrice) : null;
            Double currentValue = (quantity != null && marketPrice != null) ? roundTo4Decimals(quantity * marketPrice) : null;
            snapshot.setInvestedValue(investedValue);
            snapshot.setCurrentValue(currentValue);
            // Calculate and set totalPnlPct
            Double totalPnlPct = null;
            if (investedValue != null && investedValue != 0.0 && currentValue != null) {
                totalPnlPct = roundTo4Decimals(((currentValue - investedValue) / investedValue) * 100);
            }
            snapshot.setTotalPnlPct(totalPnlPct);
            holdingSnapshotRepository.save(snapshot);
        }
        // After sync, update user_broker_accounts with latest sync info
        account.setLastSyncDatetime(java.time.Instant.now());
        account.setLastSyncStatus("SUCCESS");
        account.setLastSyncMessage("Holdings sync completed");
        userBrokerAccountRepository.save(account);
        GenericResponse response = new GenericResponse();
        response.setStatus("success");
        response.setMessage("Holdings synced successfully");
        return response;
    }
}
