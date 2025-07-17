package com.vertiq.broker.integration.service.v1.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "brokers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Broker {

    @Id
    @Column(name = "broker_id", length = 32)
    private String brokerId;

    @Column(name = "broker_name", nullable = false)
    private String brokerName;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "creation_datetime")
    private Instant creationDateTime = Instant.now();

    @Column(name = "updation_datetime")
    private Instant updationDateTime = Instant.now();
}
