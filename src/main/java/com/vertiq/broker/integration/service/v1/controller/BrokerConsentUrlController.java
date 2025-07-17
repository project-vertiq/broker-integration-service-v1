package com.vertiq.broker.integration.service.v1.controller;

import com.vertiq.auth.v1.model.ConsentUrlResponse;
import com.vertiq.broker.integration.service.v1.service.UpstoxOAuthService;
import com.vertiq.broker.integration.service.v1.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class BrokerConsentUrlController {

    @Autowired
    private UpstoxOAuthService upstoxOAuthService;

    public ConsentUrlResponse getBrokerConsentUrl(String xUserId, String brokerId) {
        if (Constants.UPSTOX.equalsIgnoreCase(brokerId)) {
            return upstoxOAuthService.buildUpstoxConsentUrl(xUserId, brokerId);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported broker: " + brokerId);
        }
    }
}
