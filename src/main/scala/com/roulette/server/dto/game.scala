package com.roulette.server.dto

import io.circe.generic.JsonCodec

import java.util.UUID

object game {

  @JsonCodec final case class GameDto(gameId: UUID,
                                      minBetAmount: Int,
                                      maxBetAmount: Int)

  @JsonCodec final case class BetDto(id: UUID,
                                     playerId: UUID,
                                     gameId: UUID,
                                     amount: Int,
                                     combination: RouletteCombinationDto)

  @JsonCodec final case class RouletteCombinationDto(combination: String,
                                                     numbers: List[RouletteNumberDto])

  @JsonCodec final case class RouletteNumberDto(value: Int,
                                                color: String)
}
