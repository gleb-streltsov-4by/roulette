package com.roulette.server.repository

import cats.effect.Sync
import com.roulette.server.domain.game.{Game, PlayerGameSession}
import com.roulette.server.repository.impl.doobie.DoobieGameRepository
import doobie.Transactor

trait GameRepository[F[_]] {
  def findAvailableGames: F[List[Game]]
  def updateGame(game: Game): F[Game]
  def createGame(game: Game): F[Game]
  def addUserToGame(session: PlayerGameSession): F[PlayerGameSession]
  def removeUserFromGame(gameId: Int, playerId: Int): F[Int]
}

object GameRepository {
  def of[F[_]: Sync](tx: Transactor[F]): DoobieGameRepository[F] =
    new DoobieGameRepository[F](tx)
}
