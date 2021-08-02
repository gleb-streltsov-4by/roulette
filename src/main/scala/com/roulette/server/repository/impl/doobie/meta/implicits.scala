package com.roulette.server.repository.impl.doobie.meta

import doobie.Meta

import com.roulette.server.domain.game.{BetType, GameStatus, PlayerGameSessionStatus, RouletteNumber}
import com.roulette.server.util.CaseConversionUtil.{camelToSnake, snakeToCamel}

object implicits {

  implicit val gameStatusMeta: Meta[GameStatus] =
    Meta[String]
      .timap(s => GameStatus.withNameInsensitive(snakeToCamel(s.toLowerCase)))(g => normalizedSnakeCase(g.toString))

  implicit val betTypeMeta: Meta[BetType] =
    Meta[String]
      .timap(s => BetType.withNameInsensitive(snakeToCamel(s.toLowerCase)))(g => normalizedSnakeCase(g.toString))

  implicit val betDetailsMeta: Meta[List[RouletteNumber]] =
    Meta[String]
      .timap(s => s.split(",").toList.map(s => RouletteNumber(s.toInt)))(numbers =>
        numbers.map(_.value.toString).mkString(",")
      )

  implicit val gameSessionMeta: Meta[PlayerGameSessionStatus] =
    Meta[String]
      .timap(s => PlayerGameSessionStatus.withNameInsensitive(snakeToCamel(s.toLowerCase)))(g =>
        normalizedSnakeCase(g.toString)
      )

  private def normalizedSnakeCase(str: String): String = {
    val firstChar      = str.charAt(0).toLower
    val remainingChars = str.substring(1)
    val pureCamelCase  = s"$firstChar$remainingChars"

    camelToSnake(pureCamelCase).toUpperCase
  }
}
