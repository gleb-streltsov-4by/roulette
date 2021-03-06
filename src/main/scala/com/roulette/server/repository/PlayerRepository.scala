package com.roulette.server.repository

import cats.effect.Sync
import doobie.Transactor

import com.roulette.server.domain.player.Player
import com.roulette.server.repository.impl.doobie.DoobiePlayerRepository

trait PlayerRepository[F[_]] {
  def findById(playerId: Int): F[Option[Player]]
  def findAll: F[List[Player]]
}

object PlayerRepository {
  def of[F[_]: Sync](tx: Transactor[F]): DoobiePlayerRepository[F] =
    new DoobiePlayerRepository[F](tx)
}
