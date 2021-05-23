package com.roulette.server.service

import cats.effect.Sync
import com.roulette.server.dto.game.{GameDto, LeftGameDto, PlayerGameSessionDto}
import com.roulette.server.repository.GameRepository
import com.roulette.server.service.error.game.GameValidationError
import com.roulette.server.service.impl.RouletteServiceImpl

trait RouletteService[F[_]] {
  def findAvailableGames: F[List[GameDto]]
  def updateGame(game: GameDto): F[Either[GameValidationError, GameDto]]
  def createGame(game: GameDto): F[Either[GameValidationError, GameDto]]
  def addUserToGame(session: PlayerGameSessionDto): F[Either[GameValidationError, List[PlayerGameSessionDto]]]
  def removeUserFromGame(leftGame: LeftGameDto): F[Either[GameValidationError, List[PlayerGameSessionDto]]]
}

object RouletteService {
  def of[F[_]: Sync](gameRepository: GameRepository[F]): RouletteService[F] =
    new RouletteServiceImpl[F](gameRepository)
}
