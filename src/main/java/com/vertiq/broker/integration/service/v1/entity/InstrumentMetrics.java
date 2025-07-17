package com.vertiq.broker.integration.service.v1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Entity
@Table(name = "instrument_metrics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentMetrics {
    @Id
    @Column(name = "isin", length = 12)
    private String isin;

    @Column(name = "ticker", length = 32)
    private String ticker;

    @Column(name = "sector", length = 64)
    private String sector;

    @Column(name = "industry", length = 64)
    private String industry;

    @Column(name = "sub_industry", length = 64)
    private String subIndustry;

    @Column(name = "market_cap")
    private Double marketCap;

    @Column(name = "market_cap_rank")
    private Long marketCapRank;

    @Column(name = "market_cap_label", length = 32)
    private String marketCapLabel;

    @Column(name = "pe_ratio")
    private Double peRatio;

    @Column(name = "pb_ratio")
    private Double pbRatio;

    @Column(name = "industry_pe")
    private Double industryPe;

    @Column(name = "industry_pb")
    private Double industryPb;

    @Column(name = "eps")
    private Double eps;

    @Column(name = "div_yield")
    private Double divYield;

    @Column(name = "roe")
    private Double roe;

    @Column(name = "roce")
    private Double roce;

    @Column(name = "high_52_week")
    private Double high52Week;

    @Column(name = "low_52_week")
    private Double low52Week;

    @Column(name = "high_all_time")
    private Double highAllTime;

    @Column(name = "low_all_time")
    private Double lowAllTime;

    @Column(name = "beta")
    private Double beta;

    @Column(name = "alpha")
    private Double alpha;

    @Column(name = "num_shareholders")
    private Long numShareholders;

    @Column(name = "risk_label", length = 32)
    private String riskLabel;

    @Column(name = "creation_datetime")
    private Instant creationDatetime = Instant.now();

    @Column(name = "updation_datetime")
    private Instant updationDatetime = Instant.now();

    @Column(name = "source", length = 32)
    private String source;
}
