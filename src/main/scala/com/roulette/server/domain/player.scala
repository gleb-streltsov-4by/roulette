package com.roulette.server.domain

import java.util.UUID

object player {

  final case class Player(id: UUID, name: String, balance: Int)
}
