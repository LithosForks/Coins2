CREATE TABLE coin_balances(
    entity_uuid       UUID    NOT NULL,
    currency          CHAR(3) NOT NULL DEFAULT 'CNS',
    balance           BIGINT  NOT NULL DEFAULT 0,
    queued_balance    BIGINT  NOT NULL DEFAULT 0,
    received_inactive BIGINT  NOT NULL DEFAULT 0,

    PRIMARY KEY (entity_uuid, currency),

    INDEX (currency),
    INDEX (balance),
    INDEX (queued_balance),
    INDEX (received_inactive)
);
