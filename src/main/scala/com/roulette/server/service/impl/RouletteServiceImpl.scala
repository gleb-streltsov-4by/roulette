package com.roulette.server.service.impl

import cats.Monad
import cats.data.EitherT
import cats.implicits._

import com.roulette.server.core.RouletteEngine
import com.roulette.server.util.ModelMapper._
import com.roulette.server.dto.game.{GameDto, GameSessionChangeDto, PlayerGameSessionResultDto}
import com.roulette.server.repository.GameRepository
import com.roulette.server.service.RouletteService
import com.roulette.server.service.error.game.GameValidationError
import com.roulette.server.service.error.game.GameValidationError._

class RouletteServiceImpl[F[_]: Monad](
  gameRepository: GameRepository[F],
  rouletteEngine: RouletteEngine[F]
) extends RouletteService[F] {

  override def findAvailableGames: F[List[GameDto]] = for {
    availableGames <- gameRepository.findAvailableGames
  } yield availableGames.map(gameDomainToDto)

  override def updateGame(game: GameDto): F[Either[GameValidationError, GameDto]] = {
    val result: EitherT[F, GameValidationError, GameDto] = for {
      _     <- EitherT.fromOptionF(gameRepository.findById(game.id), GameNotFound(game))
      _     <- EitherT(validateGame(game).pure[F])
      domain = gameDtoToDomain(game)

      _ <- EitherT.liftF(gameRepository.updateGame(domain))
    } yield gameDomainToDto(domain)

    result.value
  }

  override def createGame(game: GameDto): F[Either[GameValidationError, GameDto]] = {
    val result: EitherT[F, GameValidationError, GameDto] = for {
      _     <- EitherT(validateGame(game).pure[F])
      domain = gameDtoToDomain(game)
      id    <- EitherT.liftF(gameRepository.createGame(domain))

      gameWithPk = domain.copy(id = id)
    } yield gameDomainToDto(gameWithPk)

    result.value
  }

  override def startGame(
    change: GameSessionChangeDto
  ): F[List[PlayerGameSessionResultDto]] = {
    for {
      gameSessions <- gameRepository.findGameSessionsByGameId(change.gameId)
      number       <- rouletteEngine.generateNumber

      results = rouletteEngine.evaluateBets(number, gameSessions)
    } yield results.map(sessionResultDomainToDto)
  }

  private def validateGame(game: GameDto): Either[GameValidationError, GameDto] = for {
    _ <- Either.cond(game.minBetAmount < game.maxBetAmount, game, InvalidGameBets(game))
  } yield game
}
