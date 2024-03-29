CREATE TABLE transactions
(
    id           VARCHAR(36)   NOT NULL UNIQUE PRIMARY KEY,
    description  VARCHAR(50)   NOT NULL,
    currency     VARCHAR(10)   NOT NULL,
    amount       DECIMAL(6, 2) NOT NULL,
    total_amount DECIMAL(6, 2) NOT NULL,
    created_at   TIMESTAMP     NOT NULL default CURRENT_TIMESTAMP
);