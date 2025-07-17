package com.vertiq.broker.integration.service.v1.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vertiq.broker.integration.service.v1.entity.UserBrokerAccount;

public interface UserBrokerAccountRepository extends JpaRepository<UserBrokerAccount, String> {

    Optional<UserBrokerAccount> findByUserIdAndBrokerId(UUID userId, String brokerId);

    List<UserBrokerAccount> findByUserId(UUID userId);
}
