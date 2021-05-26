CREATE TYPE GAME_STATUS AS ENUM ('ANNOUNCED', 'BET_SUBMISSION', 'IN_PROGRESS', 'SUSPENDED', 'ARCHIVED');

CREATE TABLE IF NOT EXISTS game (
    id SERIAL,
    name VARCHAR(32) NOT NULL,
    min_bet_amount BIGINT NOT NULL,
    max_bet_amount BIGINT NOT NULL,
    max_player_count INT NOT NULL DEFAULT 2,
    status GAME_STATUS DEFAULT 'ANNOUNCED',
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS player (
    id SERIAL,
    name VARCHAR(64) NOT NULL,
    balance INT DEFAULT 0,
    PRIMARY KEY(id)
);

CREATE UNIQUE INDEX player_name_index ON player(name);

CREATE TYPE BET_TYPE AS ENUM (
'STRAIGHT_UP', 'SPLIT', 'STREET', 'CORNER', 'LINE',             -- inside bets
'COLUMN', 'DOZEN', 'RED_OR_BLACK', 'EVEN_OR_ODD', 'LOW_OR_HIGH' -- outside bets
);

CREATE TYPE GAME_SESSION_STATUS AS ENUM ('ACTIVE', 'INACTIVE');

CREATE TABLE IF NOT EXISTS player_game_session (
    id SERIAL,
    player_id BIGINT NOT NULL,
    game_id BIGINT NOT NULL,
    is_host BOOLEAN NOT NULL,
    bet_amount INT NOT NULL,
    bet_type BET_TYPE NOT NULL,
    bet_details VARCHAR(64) NOT NULL,
    session_status GAME_SESSION_STATUS NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT player_fk FOREIGN KEY (player_id) REFERENCES player(id),
    CONSTRAINT game_fk FOREIGN KEY (game_id) REFERENCES game(id)
);

CREATE TABLE IF NOT EXISTS player_game_session (
    id SERIAL,
    player_id BIGINT NOT NULL,
    session_id BIGINT NOT NULL,
    result_number SMALLINT NOT NULL,
    bet_type BET_TYPE NOT NULL,
    is_win BOOLEAN NOT NULL,
    payoff INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT player_fk FOREIGN KEY (player_id) REFERENCES player(id),
    CONSTRAINT session_fk FOREIGN KEY (session_id) REFERENCES player_game_session(id)
);
