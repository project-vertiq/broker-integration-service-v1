-- V4__create_holdings_table.sql

CREATE TABLE holdings (
    holding_id BIGSERIAL PRIMARY KEY,
    portfolio_id VARCHAR(64) NOT NULL,
    isin VARCHAR(12) NOT NULL,
    exchange VARCHAR(8) NOT NULL,
    ticker VARCHAR(32),
    quantity DOUBLE PRECISION NOT NULL,
    avg_price DOUBLE PRECISION,
    holding_type VARCHAR(32),
    creation_datetime TIMESTAMP NOT NULL DEFAULT now(),
    updation_datetime TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT uq_portfolio_isin_exchange UNIQUE (portfolio_id, isin, exchange),
    CONSTRAINT fk_portfolio FOREIGN KEY (portfolio_id) REFERENCES user_broker_accounts(portfolio_id) ON DELETE CASCADE,
    CONSTRAINT fk_instrument FOREIGN KEY (isin, exchange) REFERENCES instruments(isin, exchange)
);
