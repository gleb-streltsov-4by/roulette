package com.roulette.server.repository.impl.doobie

import cats.effect.{Bracket, Sync}
import doobie.implicits._
import doobie.{Fragment, Transactor}
import com.roulette.server.domain.game.{Game, PlayerGameSession}
import com.roulette.server.repository.GameRepository
import meta.implicits._

class DoobieGameRepository[F[_]: Sync](tx: Transactor[F])(implicit ev: Bracket[F, Throwable])
  extends GameRepository[F] {

  private val selectGame: Fragment = fr"SELECT * FROM game"
  private val updateGame: Fragment = fr"UPDATE game"
  private val createGame: Fragment = fr"INSERT INTO game(" ++
    fr"name, min_bet_amount, max_bet_amount, max_player_count, status)"

  private val selectGameSession: Fragment = fr"SELECT * player_game_session"
  private val removeGameSession: Fragment = fr"DELETE player_game_session"
  private val createGameSession: Fragment = fr"INSERT INTO player_game_session(" ++
    fr"player_id, game_id, is_host, bet_amount, bet_type, bet_details, session_status)"

  override def findById(gameId: Int): F[Option[Game]] =
    (selectGame ++ fr"WHERE id = $gameId")
      .query[Game]
      .option
      .transact(tx)

  override def findAvailableGames: F[List[Game]] =
    (selectGame ++ fr"WHERE status = 'BET_SUBMISSION'")
      .query[Game]
      .to[List]
      .transact(tx)

  override def updateGame(game: Game): F[Int] =
    (updateGame ++
      fr"SET name = ${game.name}, " ++
      fr"min_bet_amount = ${game.minBetAmount}, " ++
      fr"max_bet_amount = ${game.maxBetAmount}, " ++
      fr"max_player_count = ${game.maxPlayerCount}, " ++
      fr"status = ${game.status} " ++
      fr"WHERE id = ${game.id}").update.run.transact(tx)

  override def createGame(game: Game): F[Int] =
    (createGame ++ fr"VALUES(" ++
      fr"${game.name}, ${game.minBetAmount}, ${game.maxBetAmount}, " ++
      fr"${game.maxPlayerCount}, ${game.status})").update.withUniqueGeneratedKeys[Int]("id").transact(tx)

  override def findGameSessionsByGameId(gameId: Int): F[List[PlayerGameSession]] =
    (selectGameSession ++ fr"WHERE gameId = $gameId, session_status = 'ACTIVE'")
      .query[PlayerGameSession]
      .to[List]
      .transact(tx)

  override def createGameSession(session: PlayerGameSession): F[Int] =
    (createGameSession ++ fr"VALUES(" ++
      fr"player_id = ${session.playerId}, " ++
      fr"game_id = ${session.gameId}, " ++
      fr"is_host = ${session.isHost}, " ++
      fr"bet_amount = ${session.betAmount}, " ++
      fr"bet_type = ${session.betType}, " ++
      fr"bet_details = ${session.betDetails}, " ++
      fr"session_status = ${session.sessionStatus})").update.withUniqueGeneratedKeys[Int]("id").transact(tx)

  override def removeGameSession(gameId: Int, playerId: Int): F[Int] =
    (removeGameSession ++
      fr"WHERE game_id = $gameId AND player_id = $playerId").update.run.transact(tx)
}
