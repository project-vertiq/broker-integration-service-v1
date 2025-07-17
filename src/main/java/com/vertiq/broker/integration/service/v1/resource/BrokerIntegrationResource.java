package com.vertiq.broker.integration.service.v1.resource;

import com.vertiq.auth.v1.api.BrokersApi;
import com.vertiq.auth.v1.model.BrokerListResponse;
import com.vertiq.auth.v1.model.CallbackRequest;
import com.vertiq.auth.v1.model.ConsentUrlResponse;
import com.vertiq.auth.v1.model.GenericResponse;
import com.vertiq.broker.integration.service.v1.controller.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrokerIntegrationResource implements BrokersApi {

    @Autowired
    BrokerConsentUrlController brokerConsentUrlController;

    @Autowired
    BrokerCallbackController brokerCallbackController;

    @Autowired
    BrokerListController brokerListController;

    @Autowired
    BrokerLogoutController brokerLogoutController;

    @Autowired
    BrokerSyncController brokerSyncController;

    @Override
    public ResponseEntity<ConsentUrlResponse> getBrokerConsentUrl(String xUserId, String brokerId) {
        return new ResponseEntity<>(brokerConsentUrlController.getBrokerConsentUrl(xUserId, brokerId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericResponse> handleBrokerCallback(String xUserId, String brokerId, CallbackRequest callbackRequest) {

        return new ResponseEntity<>(brokerCallbackController.handleBrokerCallback(xUserId, brokerId, callbackRequest), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BrokerListResponse> getBrokerList(String userId) {
        return new ResponseEntity<>(brokerListController.getBrokersForUser(userId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericResponse> disconnectBroker(String userId, String brokerId) {
        return new ResponseEntity<>(brokerLogoutController.handleBrokerLogout(userId, brokerId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericResponse> syncBrokerData(String userId, String brokerId) {
        return new ResponseEntity<>(brokerSyncController.handleBrokerSync(userId, brokerId), HttpStatus.OK);
    }



}
