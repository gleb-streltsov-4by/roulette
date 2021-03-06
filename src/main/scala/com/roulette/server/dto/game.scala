package com.roulette.server.dto

import io.circe.generic.JsonCodec

import com.roulette.server.domain.game.{BetType, GameStatus, PlayerGameSessionStatus}

object game {

  @JsonCodec
  final case class GameDto(
    id:             Int,
    name:           String,
    minBetAmount:   Int,
    maxBetAmount:   Int,
    maxPlayerCount: Int,
    status:         GameStatus
  )

  @JsonCodec
  final case class GameSessionChangeDto(gameId: Int, playerId: Int)

  @JsonCodec
  final case class PlayerGameSessionDto(
    id:            Int,
    playerId:      Int,
    gameId:        Int,
    isHost:        Boolean,
    betAmount:     Int,
    betType:       BetType,
    betDetails:    List[RouletteNumberDto],
    sessionStatus: PlayerGameSessionStatus,
  )

  @JsonCodec
  final case class RouletteNumberDto(value: Int)

  @JsonCodec
  final case class PlayerGameSessionResultDto(
    id:           Int,
    playerId:     Int,
    sessionId:    Int,
    resultNumber: RouletteNumberDto,
    betType:      BetType,
    isWin:        Boolean,
    payoff:       Int,
  )
}
