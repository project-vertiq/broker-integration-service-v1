CREATE TABLE portfolio_metrics_snapshot (
    snapshot_id BIGSERIAL PRIMARY KEY,
    portfolio_id VARCHAR(64) NOT NULL,
    date_part DATE NOT NULL,
    total_value DOUBLE PRECISION NOT NULL,
    invested_value DOUBLE PRECISION NOT NULL,
    total_returns DOUBLE PRECISION NOT NULL,
    todays_returns DOUBLE PRECISION NOT NULL,
    xirr DOUBLE PRECISION,
    avg_market_cap DOUBLE PRECISION,
    avg_pe_ratio DOUBLE PRECISION,
    avg_pb_ratio DOUBLE PRECISION,
    portfolio_alpha DOUBLE PRECISION,
    portfolio_beta DOUBLE PRECISION,
    creation_datetime TIMESTAMPTZ DEFAULT now(),
    updation_datetime TIMESTAMPTZ DEFAULT now(),
    CONSTRAINT uq_portfolio_date UNIQUE (portfolio_id, date_part),
    CONSTRAINT fk_portfolio FOREIGN KEY (portfolio_id) REFERENCES user_broker_accounts(portfolio_id) ON DELETE CASCADE
);
