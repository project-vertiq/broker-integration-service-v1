package com.vertiq.broker.integration.service.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vertiq.broker.integration.service.v1.entity.Broker;

public interface BrokerRepository extends JpaRepository<Broker, String> {
}
