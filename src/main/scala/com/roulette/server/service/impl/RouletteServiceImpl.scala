package com.roulette.server.service.impl

import cats.effect.Sync
import cats.implicits._
import com.roulette.server.dto.game
import io.scalaland.chimney.dsl._
import com.roulette.server.dto.game.{GameDto, PlayerGameSessionDto}
import com.roulette.server.repository.GameRepository
import com.roulette.server.service.RouletteService
import com.roulette.server.service.error.game.GameValidationError

class RouletteServiceImpl[F[_]: Sync](gameRepository: GameRepository[F]) extends RouletteService[F] {

  override def findAvailableGames: F[List[GameDto]] = for {
    availableGames <- gameRepository.findAvailableGames
  } yield availableGames.map(_.into[GameDto].withFieldComputed(_.status, _.status.toString).transform)

  override def updateGame(game: GameDto): F[Either[GameValidationError, GameDto]] = ???

  override def createGame(game: GameDto): F[Either[GameValidationError, GameDto]] = ???

  override def addUserToGame(session: PlayerGameSessionDto): F[Either[GameValidationError, List[PlayerGameSessionDto]]] = ???

  override def removeUserFromGame(leftGame: game.LeftGameDto): F[Either[GameValidationError, List[PlayerGameSessionDto]]] = ???
}
