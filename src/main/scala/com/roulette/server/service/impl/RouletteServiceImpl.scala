package com.roulette.server.service.impl

import cats.effect.Sync
import cats.implicits._
import com.roulette.server.core.RouletteEngine
import com.roulette.server.domain.game.{Game, GameStatus}
import com.roulette.server.dto.game
import com.roulette.server.util.DtoMapper._
import com.roulette.server.dto.game.{GameDto, PlayerGameSessionDto}
import com.roulette.server.repository.GameRepository
import com.roulette.server.service.RouletteService
import com.roulette.server.service.error.game.GameValidationError
import com.roulette.server.service.error.game.GameValidationError._

class RouletteServiceImpl[F[_]: Sync] (
  gameRepository: GameRepository[F],
  rouletteEngine: RouletteEngine[F]) extends RouletteService[F] {

  override def findAvailableGames: F[List[GameDto]] = for {
    availableGames <- gameRepository.findAvailableGames
  } yield availableGames.map(gameDomainToDto)

  override def updateGame(game: GameDto): F[Either[GameValidationError, GameDto]] = for {
    gameE     <- validateGame(game)
    updatedE  <- gameE.traverse(gameRepository.updateGame)
  } yield updatedE.map(gameDomainToDto)

  private def validateGame(game: GameDto): F[Either[GameValidationError, Game]] =
    gameRepository.findById(game.id)
      .map(gameOpt => for {
        _ <- Either.cond(gameOpt.isDefined, gameOpt.get, GameNotFound(game))
        _ <- Either.cond(game.minBetAmount < game.maxBetAmount, game, InvalidGameBets(game))

        validStatus <- GameStatus.of(game.status) match {
          case Left(_)  => Left(InvalidGameStatus(game))
          case Right(s) => Right(s)
        }

        result = gameDtoToDomain(game, validStatus)
      } yield result)

  override def createGame(game: GameDto): F[Either[GameValidationError, GameDto]] = ???

  override def addUserToGame(session: PlayerGameSessionDto): F[Either[GameValidationError, List[PlayerGameSessionDto]]] = ???

  override def removeUserFromGame(leftGame: game.LeftGameDto): F[Either[GameValidationError, List[PlayerGameSessionDto]]] = ???
}
