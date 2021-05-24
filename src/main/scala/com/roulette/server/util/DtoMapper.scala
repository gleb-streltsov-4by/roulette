package com.roulette.server.util

import com.roulette.server.domain.game.{
  BetType,
  Game,
  GameStatus,
  PlayerGameSession,
  PlayerGameSessionStatus,
  RouletteNumber
}
import com.roulette.server.dto.game.{
  GameDto,
  PlayerGameSessionDto,
  RouletteNumberDto
}
import io.scalaland.chimney.dsl._

object DtoMapper {

  def gameDomainToDto(game: Game): GameDto =
    game
      .into[GameDto]
      .withFieldComputed(_.status, _.status.toString)
      .transform

  def gameDtoToDomain(game: GameDto, status: GameStatus): Game =
    game
      .into[Game]
      .withFieldConst(_.status, status)
      .transform

  def gameSessionDomainToDto(
      gameSession: PlayerGameSession
  ): PlayerGameSessionDto =
    gameSession
      .into[PlayerGameSessionDto]
      .withFieldComputed(_.sessionStatus, _.sessionStatus.toString)
      .withFieldComputed(
        _.betDetails,
        _.betDetails.map(rouletteNumberDomainToDto)
      )
      .withFieldComputed(_.betType, _.betType.toString)
      .withFieldComputed(
        _.resultNumber,
        _.resultNumber.map(rouletteNumberDomainToDto)
      )
      .transform

  def gameSessionDtoToDomain(
      gameSession: PlayerGameSessionDto,
      status: PlayerGameSessionStatus,
      betType: BetType,
      rouletteNumbers: List[RouletteNumber],
      resultNumber: Option[RouletteNumber]
  ): PlayerGameSession =
    gameSession
      .into[PlayerGameSession]
      .withFieldConst(_.sessionStatus, status)
      .withFieldConst(_.betDetails, rouletteNumbers)
      .withFieldConst(_.betType, betType)
      .withFieldConst(_.resultNumber, resultNumber)
      .transform

  def rouletteNumberDomainToDto(
      rouletteNumber: RouletteNumber
  ): RouletteNumberDto =
    rouletteNumber
      .into[RouletteNumberDto]
      .withFieldComputed(_.color, _.color.toString)
      .transform
}
