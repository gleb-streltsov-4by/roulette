package com.roulette.server.repository

import cats.effect.Sync
import com.roulette.server.domain.game.{Game, PlayerGameSession}
import com.roulette.server.repository.impl.doobie.DoobieGameRepository
import doobie.Transactor

trait GameRepository[F[_]] {
  def findById(gameId: Int): F[Option[Game]]
  def findAvailableGames: F[List[Game]]
  def updateGame(game: Game): F[Int]
  def createGame(game: Game): F[Int]

  def createGameSession(session: PlayerGameSession): F[Int]
  def removeGameSession(gameId:  Int, playerId: Int): F[Int]
}

object GameRepository {
  def of[F[_]: Sync](tx: Transactor[F]): DoobieGameRepository[F] =
    new DoobieGameRepository[F](tx)
}
