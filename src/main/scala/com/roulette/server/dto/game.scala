package com.roulette.server.dto

import io.circe.generic.JsonCodec

object game {

  @JsonCodec final case class GameDto(id: Int,
                                      minBetAmount: Int,
                                      maxBetAmount: Int,
                                      status: String)

  @JsonCodec final case class BetDto(id: Int,
                                     playerId: Int,
                                     gameId: Int,
                                     amount: Int,
                                     combination: RouletteCombinationDto)

  @JsonCodec final case class RouletteCombinationDto(combination: String,
                                                     numbers: List[RouletteNumberDto])

  @JsonCodec final case class RouletteNumberDto(value: Int,
                                                color: String)
}
