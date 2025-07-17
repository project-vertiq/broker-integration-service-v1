package com.vertiq.broker.integration.service.v1.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "instruments")
@IdClass(InstrumentId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Instrument {
    @Id
    @Column(name = "isin", length = 32)
    private String isin;

    @Id
    @Column(name = "exchange", length = 8)
    private String exchange;

    @Column(name = "instrument_key", length = 64)
    private String instrumentKey;

    @Column(name = "symbol", length = 32)
    private String symbol;

    @Column(name = "name", length = 128)
    private String name;

    @Column(name = "instrument_type", length = 32)
    private String instrumentType;

    @Column(name = "sector", length = 64)
    private String sector;

    @Column(name = "lot_size")
    private Integer lotSize;

    @Column(name = "tick_size")
    private Double tickSize;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "exchange_token", length = 64)
    private String exchangeToken;

    @Column(name = "creation_datetime")
    private Instant creationDatetime = Instant.now();

    @Column(name = "updation_datetime")
    private Instant updationDatetime = Instant.now();
}
