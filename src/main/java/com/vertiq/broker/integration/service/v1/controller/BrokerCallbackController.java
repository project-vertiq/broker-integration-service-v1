package com.vertiq.broker.integration.service.v1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.vertiq.auth.v1.model.CallbackRequest;
import com.vertiq.auth.v1.model.GenericResponse;
import com.vertiq.broker.integration.service.v1.service.UpstoxOAuthService;

@Controller
public class BrokerCallbackController {

    private static final Logger logger = LoggerFactory.getLogger(BrokerCallbackController.class);

    @Autowired
    private UpstoxOAuthService upstoxOAuthService;

    public GenericResponse handleBrokerCallback(String xUserId, String brokerId, CallbackRequest callbackRequest) {
        logger.info("Received broker callback: xUserId={}, brokerId={}, callbackRequest={}", xUserId, brokerId, callbackRequest);
        try {
            return upstoxOAuthService.handleCallback(callbackRequest);
        } catch (Exception e) {
            logger.error("Error in handleBrokerCallback: {}", e.getMessage(), e);
            throw e;
        }
    }
}
