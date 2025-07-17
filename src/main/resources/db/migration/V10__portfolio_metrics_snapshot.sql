-- Migration to drop snapshot_id and set composite primary key on (portfolio_id, date_part)

ALTER TABLE portfolio_metrics_snapshot
DROP CONSTRAINT IF EXISTS portfolio_metrics_snapshot_pkey,
DROP CONSTRAINT IF EXISTS uq_portfolio_date,
DROP COLUMN IF EXISTS snapshot_id;

ALTER TABLE portfolio_metrics_snapshot
ADD PRIMARY KEY (portfolio_id, date_part);
