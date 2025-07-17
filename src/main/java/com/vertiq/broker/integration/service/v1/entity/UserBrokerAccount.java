package com.vertiq.broker.integration.service.v1.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "user_broker_accounts", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "broker_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBrokerAccount {

    @Id
    @Column(name = "portfolio_id", length = 64)
    private String portfolioId;

    @Column(name = "user_id", nullable = false)
    private java.util.UUID userId;

    @Column(name = "broker_id", nullable = false)
    private String brokerId;

    @Column(name = "broker_user_id")
    private String brokerUserId;

    @Column(name = "broker_user_name")
    private String brokerUserName;

    @Column(name = "broker_email")
    private String brokerEmail;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "creation_datetime")
    private Instant creationDatetime = Instant.now();

    @Column(name = "updation_datetime")
    private Instant updationDatetime = Instant.now();

    @Column(name = "last_sync_datetime")
    private Instant lastSyncDatetime;

    @Column(name = "last_sync_status")
    private String lastSyncStatus;

    @Column(name = "last_sync_message")
    private String lastSyncMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "broker_id", referencedColumnName = "broker_id", insertable = false, updatable = false)
    private Broker broker;
}
