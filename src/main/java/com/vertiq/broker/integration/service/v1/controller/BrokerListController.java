package com.vertiq.broker.integration.service.v1.controller;

import com.vertiq.auth.v1.model.BrokerListResponse;
import com.vertiq.broker.integration.service.v1.service.BrokerListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrokerListController {

    @Autowired
    private BrokerListService brokerListService;

    public BrokerListResponse getBrokersForUser(String userId) {
        return brokerListService.getBrokerList(userId);
    }
}
