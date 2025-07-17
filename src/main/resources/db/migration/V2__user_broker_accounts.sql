-- V2__create_user_broker_accounts_table.sql

CREATE TABLE user_broker_accounts (
    portfolio_id VARCHAR(64) PRIMARY KEY,
    user_id UUID NOT NULL,
    broker_id VARCHAR(32) NOT NULL,
    broker_user_id VARCHAR(128),
    broker_user_name VARCHAR(128),
    broker_email VARCHAR(128),
    is_active BOOLEAN DEFAULT TRUE,
    access_token TEXT,
    refresh_token TEXT,
    expires_at TIMESTAMP,
    creation_datetime TIMESTAMP NOT NULL DEFAULT now(),
    updation_datetime TIMESTAMP NOT NULL DEFAULT now(),
    last_sync_datetime TIMESTAMP,
    last_sync_status VARCHAR(16),
    last_sync_message TEXT,
    CONSTRAINT uq_user_broker UNIQUE (user_id, broker_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES user_auth_details(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_broker FOREIGN KEY (broker_id) REFERENCES brokers(broker_id) ON DELETE CASCADE
);
