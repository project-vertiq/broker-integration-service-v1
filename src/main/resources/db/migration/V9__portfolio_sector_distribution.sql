
-- Add market cap label percent columns to portfolio_metrics_snapshot
ALTER TABLE portfolio_metrics_snapshot
ADD COLUMN IF NOT EXISTS smallcap_percent DOUBLE PRECISION,
ADD COLUMN IF NOT EXISTS midcap_percent DOUBLE PRECISION,
ADD COLUMN IF NOT EXISTS largecap_percent DOUBLE PRECISION,
ADD COLUMN IF NOT EXISTS other_percent DOUBLE PRECISION;

-- Create portfolio_sector_distribution table without date_part
CREATE TABLE IF NOT EXISTS portfolio_sector_allocation (
    id BIGSERIAL PRIMARY KEY,
    portfolio_id VARCHAR(64) NOT NULL,
    sector VARCHAR(64) NOT NULL,
    allocation_value DOUBLE PRECISION NOT NULL,
    allocation_percent DOUBLE PRECISION NOT NULL,
    creation_datetime TIMESTAMPTZ DEFAULT now(),
    updation_datetime TIMESTAMPTZ DEFAULT now(),
    CONSTRAINT uq_portfolio_sector UNIQUE (portfolio_id, sector),
    CONSTRAINT fk_portfolio FOREIGN KEY (portfolio_id) REFERENCES user_broker_accounts(portfolio_id) ON DELETE CASCADE
);
