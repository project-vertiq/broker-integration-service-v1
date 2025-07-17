package com.vertiq.broker.integration.service.v1.controller;

import com.vertiq.auth.v1.model.GenericResponse;
import com.vertiq.broker.integration.service.v1.service.UpstoxOAuthService;
import com.vertiq.broker.integration.service.v1.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrokerLogoutController {

    @Autowired
    private UpstoxOAuthService upstoxOAuthService;

    public GenericResponse handleBrokerLogout(String userId, String brokerId) {
        if(Constants.UPSTOX.equalsIgnoreCase(brokerId)) {
            return upstoxOAuthService.handleLogout(userId, brokerId);
        }
        return null;
    }
}
