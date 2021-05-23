package com.roulette.server.repository.impl.doobie

import cats.effect.{Bracket, Sync}
import doobie.implicits._
import doobie.{Fragment, Transactor}

import com.roulette.server.domain.game.Game
import com.roulette.server.repository.GameRepository
import meta.implicits._

class DoobieGameRepository[F[_]: Sync](tx: Transactor[F])(
  implicit ev: Bracket[F, Throwable]) extends GameRepository[F] {


  private val games: Fragment = fr"SELECT * FROM game"

  override def findAvailableGames: F[List[Game]] =
    (games ++ fr"WHERE status = 'BET_SUBMISSION'").query[Game].to[List].transact(tx)
}
