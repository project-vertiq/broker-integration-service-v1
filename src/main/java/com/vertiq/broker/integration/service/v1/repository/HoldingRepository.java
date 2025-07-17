package com.vertiq.broker.integration.service.v1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vertiq.broker.integration.service.v1.entity.Holding;

public interface HoldingRepository extends JpaRepository<Holding, Long> {

    Optional<Holding> findByPortfolioIdAndIsinAndExchange(String portfolioId, String isin, String exchange);
}
