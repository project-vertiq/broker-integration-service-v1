package com.vertiq.broker.integration.service.v1.service;

import java.net.URI;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.upstox.ApiException;
import com.upstox.api.LogoutResponse;
import com.upstox.api.TokenResponse;
import com.vertiq.auth.v1.model.CallbackRequest;
import com.vertiq.auth.v1.model.ConsentUrlResponse;
import com.vertiq.auth.v1.model.GenericResponse;
import com.vertiq.broker.integration.service.v1.entity.UserBrokerAccount;
import com.vertiq.broker.integration.service.v1.repository.UserBrokerAccountRepository;
import com.vertiq.broker.integration.service.v1.utils.Constants;
import com.vertiq.broker.integration.service.v1.utils.JwtStateUtil;

import io.swagger.client.api.LoginApi;

@Service
public class UpstoxOAuthService {

    private static final Logger logger = LoggerFactory.getLogger(UpstoxOAuthService.class);
    @Value("${upstox.api-key}")
    private String clientId;

    @Value("${upstox.api-secret}")
    private String clientSecret;

    @Value("${upstox.redirect-uri}")
    private String redirectUri;

    @Value("${upstox.authorization-url:https://api-v2.upstox.com/v2/login/authorization/dialog}")
    private String authorizationUrl;

    @Value("${jwt.secret:secret}")
    private String jwtSecret;

    @Autowired
    private UserBrokerAccountRepository userBrokerAccountRepository;

    public ConsentUrlResponse buildUpstoxConsentUrl(String xUserId, String brokerId) {
        String state = JwtStateUtil.generateStateJwt(xUserId, brokerId, jwtSecret);
        String url = authorizationUrl
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code"
                + "&state=" + state;
        ConsentUrlResponse response = new ConsentUrlResponse();
        response.setAuthorizationUrl(URI.create(url));
        return response;
    }

    public GenericResponse handleCallback(CallbackRequest callbackRequest) {
        String state = callbackRequest.getState();
        String code = callbackRequest.getCode();
        logger.info("handleCallback called with state={}, code={}", state, code);
        Map<String, Object> stateClaims;
        try {
            stateClaims = JwtStateUtil.parseStateJwt(state, jwtSecret);
        } catch (Exception e) {
            logger.error("Invalid or expired state parameter: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired state parameter");
        }
        String brokerId = (String) stateClaims.get("brokerId");
        String userIdStr = (String) stateClaims.get("userId");
        logger.info("Parsed state claims: brokerId={}, userIdStr={}", brokerId, userIdStr);
        UUID userId = UUID.fromString(userIdStr);

        // Use Upstox SDK for token exchange
        LoginApi apiInstance = new LoginApi();
        String apiVersion = "2.0";
        String grantType = "authorization_code";
        TokenResponse tokenResponse;
        try {
            tokenResponse = apiInstance.token(apiVersion, code, clientId, clientSecret, redirectUri, grantType);
        } catch (ApiException e) {
            logger.error("Failed to exchange code for token: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to exchange code for token: " + e.getMessage());
        }
        String accessToken = tokenResponse.getAccessToken();
        logger.info("Received access token for userId={}, brokerId={}", userId, brokerId);
        // Parse expiry from JWT access token
        Instant expiresAt = null;
        try {
            Long expiryEpoch = com.vertiq.broker.integration.service.v1.utils.JwtAccessTokenUtil.getExpiryEpochSeconds(accessToken);
            if (expiryEpoch != null) {
                expiresAt = Instant.ofEpochSecond(expiryEpoch);
                logger.info("Parsed access token expiry: {}", expiresAt);
            } else {
                logger.warn("No expiry found in access token JWT");
            }
        } catch (Exception e) {
            logger.error("Failed to parse expiry from access token JWT: {}", e.getMessage(), e);
        }
        // Store in DB
        UserBrokerAccount account = userBrokerAccountRepository.findByUserIdAndBrokerId(userId, brokerId)
                .orElse(new UserBrokerAccount());
        account.setUserId(userId);
        account.setBrokerId(brokerId);
        account.setBrokerEmail(tokenResponse.getEmail());
        account.setBrokerUserId(tokenResponse.getUserId());
        account.setBrokerUserName(tokenResponse.getUserName());
        account.setAccessToken(accessToken);
        account.setExpiresAt(expiresAt);
        account.setIsActive(tokenResponse.isIsActive());
        account.setUpdationDatetime(Instant.now());
        if (account.getCreationDatetime() == null) {
            account.setCreationDatetime(Instant.now());
        }
        // Set portfolioId as broker_id + '_' + broker_user_id if not already set
        if (account.getPortfolioId() == null || account.getPortfolioId().isEmpty()) {
            String brokerUserId = tokenResponse.getUserId();
            if (brokerUserId == null || brokerUserId.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Broker user ID is missing, cannot generate portfolio_id");
            }
            account.setPortfolioId(brokerId + "_" + brokerUserId);
        }
        userBrokerAccountRepository.save(account);
        logger.info("User broker account saved for userId={}, brokerId={}", userId, brokerId);
        GenericResponse response = new GenericResponse();
        response.setStatus("success");
        response.setMessage("Broker connection established and tokens saved.");
        return response;
    }

    public GenericResponse handleLogout(String xUserId, String brokerId) {
        UUID userId = UUID.fromString(xUserId);
        Optional<UserBrokerAccount> accountOpt = userBrokerAccountRepository.findByUserIdAndBrokerId(userId, brokerId);
        if (accountOpt.isPresent()) {
            String accessToken = accountOpt.get().getAccessToken();
            String apiVersion = "2.0";
            try {
                LoginApi apiInstance = new LoginApi();
                apiInstance.getApiClient().addDefaultHeader("Authorization", "Bearer " + accessToken);
                LogoutResponse result = null;
                try {
                    result = apiInstance.logout(apiVersion);
                    logger.info("Upstox logout API response: {}", result);
                } catch (com.upstox.ApiException apiEx) {
                    logger.error("Upstox logout API exception: {}", apiEx.getMessage(), apiEx);
                    if (apiEx.getCode() == 401) {
                        // Token already invalid/expired, treat as success
                        UserBrokerAccount account = accountOpt.get();
                        account.setAccessToken(null);
                        account.setRefreshToken(null);
                        account.setIsActive(false);
                        userBrokerAccountRepository.save(account);
                        logger.info("Upstox logout returned 401 (already logged out/invalid), deactivated account for userId={}, brokerId={}", userId, brokerId);
                        GenericResponse response = new GenericResponse();
                        response.setStatus("success");
                        response.setMessage("Upstox token already invalid, account deactivated.");
                        return response;
                    } else {
                        throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Upstox logout API exception: " + apiEx.getMessage());
                    }
                }
                if (result != null && Constants.SUCCESS.equalsIgnoreCase(String.valueOf(result.getStatus())) && result.isData()) {
                    UserBrokerAccount account = accountOpt.get();
                    account.setAccessToken(null);
                    account.setRefreshToken(null);
                    account.setIsActive(false);
                    account.setUpdationDatetime(Instant.now());
                    userBrokerAccountRepository.save(account);
                    logger.info("Upstox logout API success and account deactivated for userId={}, brokerId={}", userId, brokerId);
                    GenericResponse response = new GenericResponse();
                    response.setStatus("success");
                    response.setMessage("Upstox logout successful and account deactivated.");
                    return response;
                } else {
                    logger.error("Upstox logout API failed or returned non-success for userId={}, brokerId={}", userId, brokerId);
                    throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Upstox logout API failed");
                }
            } catch (Exception e) {
                logger.error("Exception during Upstox logout for userId={}, brokerId={}: {}", userId, brokerId, e.getMessage(), e);
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Upstox logout API exception: " + e.getMessage());
            }
        } else {
            logger.warn("No Upstox account found for userId={}, brokerId={}", userId, brokerId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Upstox account found for user");
        }
    }
}
