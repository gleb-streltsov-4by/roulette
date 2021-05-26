package com.roulette.server.repository.impl.doobie.meta

import com.roulette.server.domain.game.GameStatus
import com.roulette.server.util.CaseConversionUtil.{camelToSnake, snakeToCamel}
import doobie.Meta

object implicits {

  implicit val gameStatusMeta: Meta[GameStatus] =
    Meta[String]
      .timap(s => GameStatus.withNameInsensitive(snakeToCamel(s.toLowerCase)))(g => {
        val str            = g.toString
        val firstChar      = str.charAt(0).toLower
        val remainingChars = str.substring(1)

        val pureCamelCase = s"$firstChar$remainingChars"

        camelToSnake(pureCamelCase).toUpperCase
      })
}
