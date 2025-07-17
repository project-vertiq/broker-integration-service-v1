-- V4__create_instruments_table.sql

CREATE TABLE instruments (
    isin VARCHAR(32) NOT NULL,
    exchange VARCHAR(8) NOT NULL,
    instrument_key VARCHAR(64),
    exchange_token VARCHAR(64),
    symbol VARCHAR(32) NOT NULL,
    name VARCHAR(128) NOT NULL,
    instrument_type VARCHAR(32),
    sector VARCHAR(64),
    lot_size INTEGER,
    tick_size DOUBLE PRECISION,
    is_active BOOLEAN DEFAULT TRUE,
    creation_datetime TIMESTAMPTZ DEFAULT now(),
    updation_datetime TIMESTAMPTZ DEFAULT now(),
    PRIMARY KEY (isin, exchange)
);
