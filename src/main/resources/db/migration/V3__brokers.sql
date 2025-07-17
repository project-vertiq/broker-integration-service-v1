-- V3__insert_brokers.sql

INSERT INTO brokers (broker_id, broker_name, logo_url, website_url, is_active, creation_datetime, updation_datetime) VALUES
  ('UPST', 'Upstox', 'https://your-cdn.com/logos/upstox_only_logo.jpeg', 'https://upstox.com', TRUE, now(), now()),
  ('ZERO', 'Zerodha', 'https://your-cdn.com/logos/zerodha_only_logo.jpeg', 'https://zerodha.com', TRUE, now(), now()),
  ('GROW', 'Groww', 'https://your-cdn.com/logos/groww_only_logo.png', 'https://groww.in', TRUE, now(), now()),
  ('ANGE', 'Angel One', 'https://your-cdn.com/logos/angelone_only_logo.jpeg', 'https://angelone.in', TRUE, now(), now());
