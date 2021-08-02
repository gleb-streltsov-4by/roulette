package com.roulette.server.repository

import cats.effect.Sync
import doobie.Transactor

import com.roulette.server.domain.game.{Game, PlayerGameSession}
import com.roulette.server.repository.impl.doobie.DoobieGameRepository

trait GameRepository[F[_]] {
  def findById(gameId: Int): F[Option[Game]]
  def findAvailableGames: F[List[Game]]
  def updateGame(game: Game): F[Int]
  def createGame(game: Game): F[Int]

  def findGameSessionsByGameId(gameId: Int): F[List[PlayerGameSession]]

  def createGameSession(session: PlayerGameSession): F[Int]
  def removeGameSession(gameId:  Int, playerId: Int): F[Int]
}

object GameRepository {
  def of[F[_]: Sync](tx: Transactor[F]): DoobieGameRepository[F] =
    new DoobieGameRepository[F](tx)
}
