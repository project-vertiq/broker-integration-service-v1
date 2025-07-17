package com.vertiq.broker.integration.service.v1.entity;

import java.time.Instant;
import java.time.LocalDate;

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
@Table(name = "holdings_snapshot", uniqueConstraints = @UniqueConstraint(columnNames = {"holding_id", "date_part"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoldingSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "snapshot_id")
    private Long snapshotId;

    @Column(name = "holding_id", nullable = false)
    private Long holdingId;

    @Column(name = "date_part", nullable = false)
    private LocalDate datePart;

    @Column(name = "day_change")
    private Double dayChange;

    @Column(name = "day_change_pct")
    private Double dayChangePct;

    @Column(name = "total_pnl")
    private Double totalPnl;

    @Column(name = "total_pnl_pct")
    private Double totalPnlPct;

    @Column(name = "market_price")
    private Double marketPrice;

    @Column(name = "pe_ratio")
    private Double peRatio;

    @Column(name = "pb_ratio")
    private Double pbRatio;

    @Column(name = "market_cap")
    private Double marketCap;

    @Column(name = "creation_datetime")
    private Instant creationDatetime = Instant.now();

    @Column(name = "updation_datetime")
    private Instant updationDatetime = Instant.now();

    @Column(name = "invested_value")
    private Double investedValue;

    @Column(name = "current_value")
    private Double currentValue;
}
