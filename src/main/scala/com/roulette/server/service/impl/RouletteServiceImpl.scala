package com.roulette.server.service.impl

import cats.data.EitherT
import cats.effect.Sync
import cats.implicits._
import com.roulette.server.core.RouletteEngine
import com.roulette.server.domain.game.{Game, PlayerGameSession}
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

  override def updateGame(game: GameDto): F[Either[GameValidationError, GameDto]] = {
    val result: EitherT[F, GameValidationError, GameDto] = for {
      _    <- EitherT.fromOptionF(gameRepository.findById(game.id), GameNotFound(game))
      game <- EitherT(validateGame(game).pure[F])
      _    <- EitherT.liftF(gameRepository.updateGame(game))
    } yield gameDomainToDto(game)

    result.value
  }

  override def createGame(game: GameDto): F[Either[GameValidationError, GameDto]] = {
    val result: EitherT[F, GameValidationError, GameDto] = for {
      game <- EitherT(validateGame(game).pure[F])
      id   <- EitherT.liftF(gameRepository.createGame(game))

      gameWithPk = game.copy(id = id)
    } yield gameDomainToDto(gameWithPk)

    result.value
  }

  override def startGame(gameId: Int): F[Either[GameValidationError, List[PlayerGameSessionResultDto]]] = {
    val result: EitherT[F, GameValidationError, List[PlayerGameSessionResultDto]] =
      for {
        gameSessions <- EitherT(validateGameSessions(gameId))
        number       <- EitherT.liftF(rouletteEngine.generateNumber)

        results = rouletteEngine.evaluateBets(number, gameSessions)
      } yield results.map(sessionResultDomainToDto)

    result.value
  }

  override def addUserToGame(
    session: PlayerGameSessionDto
  ): F[Either[GameValidationError, List[PlayerGameSessionDto]]] = ???

  override def removeUserFromGame(
    leftGame: game.LeftGameDto
  ): F[Either[GameValidationError, List[PlayerGameSessionDto]]] = ???

  private def validateGame(game: GameDto): Either[GameValidationError, Game] = for {
    _ <- Either.cond(game.minBetAmount < game.maxBetAmount, game, InvalidGameBets(game))
  } yield gameDtoToDomain(game)

  private def validateGameSessions(
    gameId: Int
  ): F[Either[GameValidationError, List[PlayerGameSession]]] = ???
}
