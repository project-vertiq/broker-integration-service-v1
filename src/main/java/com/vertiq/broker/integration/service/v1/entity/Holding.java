package com.vertiq.broker.integration.service.v1.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "holdings", uniqueConstraints = @UniqueConstraint(columnNames = {"portfolio_id", "isin", "exchange"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "holding_id")
    private Long holdingId;

    @Column(name = "portfolio_id", length = 64, nullable = false)
    private String portfolioId;

    @Column(name = "isin", length = 12, nullable = false)
    private String isin;

    @Column(name = "exchange", length = 8, nullable = false)
    private String exchange;

    @Column(name = "ticker", length = 32)
    private String ticker;

    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @Column(name = "avg_price")
    private Double avgPrice;

    @Column(name = "holding_type")
    private String holdingType;

    @Column(name = "creation_datetime")
    private Instant creationDatetime = Instant.now();

    @Column(name = "updation_datetime")
    private Instant updationDatetime = Instant.now();
}
