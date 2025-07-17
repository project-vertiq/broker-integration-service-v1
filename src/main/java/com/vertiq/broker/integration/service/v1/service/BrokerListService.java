package com.vertiq.broker.integration.service.v1.service;

import com.vertiq.auth.v1.model.BrokerDetails;
import com.vertiq.auth.v1.model.BrokerListResponse;
import com.vertiq.broker.integration.service.v1.entity.Broker;
import com.vertiq.broker.integration.service.v1.entity.UserBrokerAccount;
import com.vertiq.broker.integration.service.v1.repository.UserBrokerAccountRepository;
import com.vertiq.broker.integration.service.v1.repository.BrokerRepository;
import com.vertiq.broker.integration.service.v1.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BrokerListService {

    @Autowired
    private UserBrokerAccountRepository userBrokerAccountRepository;

    @Autowired
    private BrokerRepository brokerRepository;

    public BrokerListResponse getBrokerList(String userId) {
        UUID userUUID = UUID.fromString(userId);
        List<Broker> allBrokers = brokerRepository.findAll();
        List<UserBrokerAccount> userAccounts = userBrokerAccountRepository.findByUserId(userUUID);
        Map<String, UserBrokerAccount> accountMap = userAccounts.stream()
                .collect(Collectors.toMap(UserBrokerAccount::getBrokerId, acc -> acc));
        BrokerListResponse response = new BrokerListResponse();
        for (Broker broker : allBrokers) {
            BrokerDetails brokerDetails = new BrokerDetails();
            brokerDetails.setBrokerId(broker.getBrokerId());
            brokerDetails.setBrokerName(broker.getBrokerName());
            brokerDetails.setLogoUrl(broker.getLogoUrl());
            UserBrokerAccount account = accountMap.get(broker.getBrokerId());
            if (account != null) {
                brokerDetails.setIsConnected(account.getExpiresAt() != null && account.getExpiresAt().isAfter(Instant.now()));
                brokerDetails.setLastSync(account.getLastSyncDatetime() != null ? OffsetDateTime.ofInstant(account.getLastSyncDatetime(), java.time.ZoneOffset.UTC) : null);
                brokerDetails.setExpiry(account.getExpiresAt() != null ? OffsetDateTime.ofInstant(account.getExpiresAt(), java.time.ZoneOffset.UTC) : null);
                brokerDetails.setLastSyncStatus(Constants.SUCCESS.equalsIgnoreCase(account.getLastSyncStatus()));
            } else {
                brokerDetails.setIsConnected(false);
            }
            response.getBrokers().add(brokerDetails);
        }
        return response;
    }
}
