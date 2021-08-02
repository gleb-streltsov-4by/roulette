package com.roulette.server.util

import io.scalaland.chimney.dsl._

import com.roulette.server.domain.game.{Game, PlayerGameSession, PlayerGameSessionResult, RouletteNumber}
import com.roulette.server.dto.game.{GameDto, PlayerGameSessionDto, PlayerGameSessionResultDto, RouletteNumberDto}

object ModelMapper {

  def gameSessionToResult(
    gameSession:  PlayerGameSession,
    resultNumber: RouletteNumber,
    isWin:        Boolean,
    payoff:       Int
  ): PlayerGameSessionResult =
    gameSession
      .into[PlayerGameSessionResult]
      .withFieldComputed(_.sessionId, _.id)
      .withFieldConst(_.resultNumber, resultNumber)
      .withFieldConst(_.isWin, isWin)
      .withFieldConst(_.payoff, payoff)
      .transform

  def gameDomainToDto(game: Game): GameDto =
    game
      .into[GameDto]
      .transform

  def gameDtoToDomain(game: GameDto): Game =
    game
      .into[Game]
      .transform

  def gameSessionDomainToDto(gameSession: PlayerGameSession): PlayerGameSessionDto =
    gameSession
      .into[PlayerGameSessionDto]
      .transform

  def gameSessionDtoToDomain(gameSession: PlayerGameSessionDto): PlayerGameSession =
    gameSession
      .into[PlayerGameSession]
      .transform

  def rouletteNumberDomainToDto(rouletteNumber: RouletteNumber): RouletteNumberDto =
    rouletteNumber
      .into[RouletteNumberDto]
      .transform

  def rouletteNumberDtoToDomain(rouletteNumber: RouletteNumberDto): RouletteNumber =
    rouletteNumber
      .into[RouletteNumber]
      .transform

  def sessionResultDomainToDto(result: PlayerGameSessionResult): PlayerGameSessionResultDto =
    result
      .into[PlayerGameSessionResultDto]
      .transform

  def sessionResultDtoToDomain(result: PlayerGameSessionResultDto): PlayerGameSessionResult =
    result
      .into[PlayerGameSessionResult]
      .transform
}
