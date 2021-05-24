INSERT INTO game(id, name, min_bet_amount, max_bet_amount, max_player_count, status)
VALUES
(1, 'game1', 100, 1000, 4, 'ANNOUNCED'),
(2, 'game2', 50, 500, 3, 'BET_SUBMISSION'),
(3, 'game3', 400, 800, 2, 'BET_SUBMISSION'),
(4, 'game4', 500, 1000, 6, 'IN_PROGRESS'),
(5, 'game5', 500, 1000, 8, 'SUSPENDED'),
(6, 'game6', 1000, 2000, 4, 'ARCHIVED');

INSERT INTO player(id, name, balance)
VALUES
(1, 'player1', 500),
(2, 'player2', 700),
(3, 'player3', 100),
(4, 'player4', 2000),
(5, 'player5', 950),
(6, 'player6', 300);

INSERT INTO player_game_session(id, player_id, game_id, is_host, bet_amount, bet_type, bet_details, session_status, result_number)
VALUES
(1, 1, 2, 'TRUE', 100, 'STRAIGHT_UP', '1', 'ACTIVE', NULL),
(2, 2, 2, 'FALSE',  50, 'SPLIT', '11,14', 'ACTIVE', NULL),
(3, 3, 2, 'FALSE', 50, 'STREET', '19-21', 'ACTIVE', NULL),

(4, 4, 3, 'TRUE', 400, 'CORNER', '25,26,28,29', 'ACTIVE', NULL),
(5, 5, 3, 'FALSE', 400, 'RED_OR_BLACK', 'R', 'ACTIVE', NULL),

(6, 2, 6, 'TRUE', 1000, 'STRAIGHT_UP', '1', 'INACTIVE', 1),
(7, 3, 6, 'FALSE', 1000, 'COLUMN', '2', 'INACTIVE', 1),
(8, 4, 6, 'FALSE', 1000, 'DOZEN', '25-36', 'INACTIVE', 1),
(9, 1, 6, 'FALSE', 1000, 'EVEN_OR_ODD', 'E', 'INACTIVE', 1);
