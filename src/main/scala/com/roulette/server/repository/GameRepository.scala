package com.roulette.server.repository

import cats.effect.Sync
import com.roulette.server.domain.game.Game
import com.roulette.server.repository.impl.DoobieGameRepository
import doobie.Transactor

trait GameRepository[F[_]] {
  def findAvailableGames: F[List[Game]]
}

object GameRepository {
  def of[F[_]: Sync](tx: Transactor[F]): DoobieGameRepository[F] =
    new DoobieGameRepository[F](tx)
}
