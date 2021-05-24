package com.roulette.server.util

import com.roulette.server.domain.game.Game
import com.roulette.server.dto.game.GameDto
import io.scalaland.chimney.dsl._

object DtoMapper {

  def gameDomainToDto(game: Game): GameDto =
    game.into[GameDto]
      .withFieldComputed(_.status, _.status.toString)
      .transform
}
