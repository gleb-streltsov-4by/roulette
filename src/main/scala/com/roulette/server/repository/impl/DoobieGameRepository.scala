package com.roulette.server.repository.impl

import cats.effect.{Bracket, Sync}
import com.roulette.server.domain.game.Game
import com.roulette.server.repository.GameRepository
import doobie.{Fragment, Meta, Transactor}
import doobie.implicits._

import java.util.UUID

class DoobieGameRepository[F[_]: Sync](tx: Transactor[F])(
  implicit ev: Bracket[F, Throwable]) extends GameRepository[F] {

  private implicit val uuidMeta: Meta[UUID] = Meta[String].timap(UUID.fromString)(_.toString)

  private val games: Fragment = fr"SELECT id, min_bet_amount, max_bet_amount FROM games"

  override def findAvailableGames: F[List[Game]] =
    games.query[Game].to[List].transact(tx)
    //(games ++ fr"WHERE status = 'BETS_SUBMISSION'").query[Game].to[List].transact(tx)
}
