package com.roulette.server.dto

import io.circe.generic.JsonCodec

object player {

  @JsonCodec final case class PlayerDto(id: BigInt, name: String, balance: Int)
}
