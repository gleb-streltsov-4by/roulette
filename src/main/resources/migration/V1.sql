CREATE TABLE IF NOT EXISTS games (
    id serial,
    min_bet_amount bigint not null,
    max_bet_amount bigint not null,
    PRIMARY KEY(id)
);

INSERT INTO games(id, min_bet_amount, max_bet_amount)
VALUES
(1, 100, 1000),
(2, 50, 500),
(3, 400, 800);