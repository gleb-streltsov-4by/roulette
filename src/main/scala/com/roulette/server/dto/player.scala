package com.roulette.server.dto

import io.circe.generic.JsonCodec

import java.util.UUID

object player {

  @JsonCodec final case class PlayerDto(id: UUID, name: String, balance: Int)
}
