package com.vertiq.broker.integration.service.v1.controller;

import com.vertiq.auth.v1.model.GenericResponse;
import com.vertiq.broker.integration.service.v1.service.UpstoxOAuthService;
import com.vertiq.broker.integration.service.v1.service.UpstoxPortfolioService;
import com.vertiq.broker.integration.service.v1.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrokerSyncController {

    @Autowired
    private UpstoxPortfolioService upstoxPortfolioService;

    public GenericResponse handleBrokerSync(String userId, String brokerId) {
        if(Constants.UPSTOX.equalsIgnoreCase(brokerId)) {
            return upstoxPortfolioService.handleBrokerDataSync(userId, brokerId);
        }
        return null;
    }
}
