package com.roulette.server.dto

import io.circe.generic.JsonCodec

object game {

  @JsonCodec final case class GameDto(
    id:             Int,
    name:           String,
    minBetAmount:   Int,
    maxBetAmount:   Int,
    maxPlayerCount: Int,
    status:         String
  )

  @JsonCodec final case class LeftGameDto(gameId: Int, playerId: Int)

  @JsonCodec final case class PlayerGameSessionDto(
    id:            Int,
    playerId:      Int,
    gameId:        Int,
    isHost:        Boolean,
    betAmount:     Int,
    betType:       String,
    betDetails:    List[RouletteNumberDto],
    sessionStatus: String,
    resultNumber:  Option[RouletteNumberDto]
  )

  @JsonCodec final case class RouletteNumberDto(value: Int, color: String)
}
