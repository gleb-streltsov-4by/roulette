package com.roulette.server.service

import cats.effect.Sync
import com.roulette.server.core.RouletteEngine
import com.roulette.server.dto.game.{GameDto, GameSessionChangeDto, PlayerGameSessionDto, PlayerGameSessionResultDto}
import com.roulette.server.repository.GameRepository
import com.roulette.server.service.error.game.GameValidationError
import com.roulette.server.service.impl.RouletteServiceImpl

trait RouletteService[F[_]] {
  def findAvailableGames: F[List[GameDto]]
  def updateGame(game: GameDto): F[Either[GameValidationError, GameDto]]
  def createGame(game: GameDto): F[Either[GameValidationError, GameDto]]

  def addUserToGame(
    session: PlayerGameSessionDto
  ): F[Either[GameValidationError, List[PlayerGameSessionDto]]]

  def removeUserFromGame(
    changeGame: GameSessionChangeDto
  ): F[Either[GameValidationError, List[PlayerGameSessionDto]]]

  def startGame(
    changeGame: GameSessionChangeDto
  ): F[List[PlayerGameSessionResultDto]]
}

object RouletteService {
  def of[F[_]: Sync](
    gameRepository: GameRepository[F],
    rouletteEngine: RouletteEngine[F]
  ): RouletteService[F] =
    new RouletteServiceImpl[F](gameRepository, rouletteEngine)
}
