-- V1__create_brokers_table.sql

CREATE TABLE brokers (
    broker_id VARCHAR(32) PRIMARY KEY,
    broker_name VARCHAR(64) NOT NULL,
    logo_url TEXT,
    website_url TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    creation_datetime TIMESTAMP DEFAULT now(),
    updation_datetime TIMESTAMP DEFAULT now()
);
