-- V5__create_holdings_snapshot_table.sql

CREATE TABLE holdings_snapshot (
    snapshot_id BIGSERIAL PRIMARY KEY,
    holding_id BIGSERIAL NOT NULL,
    date_part DATE NOT NULL,

    day_change DOUBLE PRECISION,
    day_change_pct DOUBLE PRECISION,
    total_pnl DOUBLE PRECISION,
    total_pnl_pct DOUBLE PRECISION,

    market_price DOUBLE PRECISION,
    pe_ratio DOUBLE PRECISION,
    pb_ratio DOUBLE PRECISION,
    market_cap DOUBLE PRECISION,

    invested_value DOUBLE PRECISION,
    current_value DOUBLE PRECISION,

    creation_datetime TIMESTAMPTZ DEFAULT now(),
    updation_datetime TIMESTAMPTZ DEFAULT now(),

    CONSTRAINT uq_holding_date UNIQUE (holding_id, date_part),
    CONSTRAINT fk_holding FOREIGN KEY (holding_id) REFERENCES holdings(holding_id) ON DELETE CASCADE
);
