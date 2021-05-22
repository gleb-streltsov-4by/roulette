package com.roulette.server.repository.impl.doobie.meta

import com.roulette.server.domain.game.GameStatus
import com.roulette.server.util.CaseConversionUtil.{camelToSnake, snakeToCamel}
import doobie.Meta

object implicits {

  implicit val gameStatusMeta: Meta[GameStatus] =
    Meta[String].timap(
      s => GameStatus.withNameInsensitive(snakeToCamel(s.toLowerCase)))(
      g => {
        val str = g.toString
        val pureCamelCase = str.charAt(0).toLower + str.substring(1)

        camelToSnake(pureCamelCase).toUpperCase
      })
}
