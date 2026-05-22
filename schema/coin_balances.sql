CREATE TABLE IF NOT EXISTS coin_balances(
    uuid              CHAR(36)       NOT NULL,
    currency          VARCHAR(50)    NOT NULL,
    balance           DECIMAL(19, 6) NOT NULL DEFAULT 0,
    queued_balance    DECIMAL(19, 6) NOT NULL DEFAULT 0,
    received_inactive DECIMAL(19, 6) NOT NULL DEFAULT 0,
    PRIMARY KEY (uuid, currency),
    INDEX (currency),
    INDEX (balance),
    INDEX (queued_balance),
    INDEX (received_inactive)
);
