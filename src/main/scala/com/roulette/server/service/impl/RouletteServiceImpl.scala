package com.roulette.server.service.impl

import cats.data.EitherT
import cats.effect.Sync
import cats.implicits._
import com.roulette.server.core.RouletteEngine
import com.roulette.server.domain.game.{Game, GameStatus, PlayerGameSession}
import com.roulette.server.dto.game
import com.roulette.server.util.ModelMapper._
import com.roulette.server.dto.game.{GameDto, PlayerGameSessionDto, PlayerGameSessionResultDto}
import com.roulette.server.repository.GameRepository
import com.roulette.server.service.RouletteService
import com.roulette.server.service.error.game.GameValidationError
import com.roulette.server.service.error.game.GameValidationError._

class RouletteServiceImpl[F[_]: Sync](
  gameRepository: GameRepository[F],
  rouletteEngine: RouletteEngine[F]
) extends RouletteService[F] {

  override def findAvailableGames: F[List[GameDto]] = for {
    availableGames <- gameRepository.findAvailableGames
  } yield availableGames.map(gameDomainToDto)

  override def updateGame(
    game: GameDto
  ): F[Either[GameValidationError, GameDto]] = for {
    gameE    <- validateGame(game)
    updatedE <- gameE.traverse(gameRepository.updateGame)
  } yield updatedE.map(gameDomainToDto)

  override def startGame(
    gameId: Int
  ): F[Either[GameValidationError, List[PlayerGameSessionResultDto]]] = {
    val result: EitherT[F, GameValidationError, List[PlayerGameSessionResultDto]] =
      for {
        gameSessions <- EitherT(validateGameSessions(gameId))
        number       <- EitherT.liftF(rouletteEngine.generateNumber)

        results = rouletteEngine.evaluateBets(number, gameSessions)
      } yield results.map(sessionResultDomainToDto)

    result.value
  }

  private def validateGame(
    game: GameDto
  ): F[Either[GameValidationError, Game]] =
    gameRepository
      .findById(game.id)
      .map(gameOpt =>
        for {
          _ <- Either.fromOption(gameOpt, GameNotFound(game))
          _ <- Either.cond(
            game.minBetAmount < game.maxBetAmount,
            game,
            InvalidGameBets(game)
          )
        } yield gameDtoToDomain(game)
      )

  override def createGame(
    game: GameDto
  ): F[Either[GameValidationError, GameDto]] = ???

  override def addUserToGame(
    session: PlayerGameSessionDto
  ): F[Either[GameValidationError, List[PlayerGameSessionDto]]] = ???

  override def removeUserFromGame(
    leftGame: game.LeftGameDto
  ): F[Either[GameValidationError, List[PlayerGameSessionDto]]] = ???

  private def validateGameSessions(
    gameId: Int
  ): F[Either[GameValidationError, List[PlayerGameSession]]] = ???

  private def check(): F[Unit] = for {
    number <- rouletteEngine.generateNumber
  } yield ()
}
