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

INSERT INTO player_game_session(id, player_id, game_id, is_host, bet_amount, bet_type, bet_details, session_status)
VALUES
(1, 1, 2, 'TRUE', 100, 'STRAIGHT_UP', '1', 'ACTIVE'),
(2, 2, 2, 'FALSE',  50, 'SPLIT', '11,14', 'ACTIVE'),
(3, 3, 2, 'FALSE', 50, 'STREET', '19,20,21', 'ACTIVE'),

(4, 4, 3, 'TRUE', 400, 'CORNER', '25,26,28,29', 'ACTIVE'),
(5, 5, 3, 'FALSE', 400, 'RED_OR_BLACK', '1,3,5,7,9,12,14,16,18,19,21,23,25,27,30,32,34,36', 'ACTIVE'),

(6, 2, 6, 'TRUE', 1000, 'STRAIGHT_UP', '1', 'INACTIVE'),
(7, 3, 6, 'FALSE', 1000, 'COLUMN', '2', 'INACTIVE'),
(8, 4, 6, 'FALSE', 1000, 'DOZEN', '25,26,27,28,29,30,31,32,33,34,35,36', 'INACTIVE'),
(9, 1, 6, 'FALSE', 1000, 'EVEN_OR_ODD', '2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32,34,36', 'INACTIVE');
