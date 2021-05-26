package com.roulette.server.domain

import enumeratum.{Enum, EnumEntry}

object game {

  final case class Game(
    id:             Int,
    name:           String,
    minBetAmount:   Int,
    maxBetAmount:   Int,
    maxPlayerCount: Int,
    status:         GameStatus
  )

  sealed trait GameStatus extends EnumEntry
  object GameStatus extends Enum[GameStatus] {

    val values: IndexedSeq[GameStatus] = findValues

    final case object Announced extends GameStatus
    final case object BetSubmission extends GameStatus
    final case object InProgress extends GameStatus
    final case object Suspended extends GameStatus
    final case object Archived extends GameStatus

    def of(status: String): Either[String, GameStatus] = {
      GameStatus
        .withNameInsensitiveOption(status)
        .toRight(
          s"Game status `$status` is invalid... Available are: ${GameStatus.values}"
        )
    }
  }

  final case class PlayerGameSession(
    id:            Int,
    playerId:      Int,
    gameId:        Int,
    isHost:        Boolean,
    betAmount:     Int,
    betType:       BetType,
    betDetails:    List[RouletteNumber],
    sessionStatus: PlayerGameSessionStatus,
    resultNumber:  Option[RouletteNumber]
  )

  sealed trait PlayerGameSessionStatus extends EnumEntry
  object PlayerGameSessionStatus extends Enum[PlayerGameSessionStatus] {

    val values: IndexedSeq[PlayerGameSessionStatus] = findValues

    final case object ACTIVE extends PlayerGameSessionStatus
    final case object INACTIVE extends PlayerGameSessionStatus

    def of(status: String): Either[String, PlayerGameSessionStatus] = {
      PlayerGameSessionStatus
        .withNameInsensitiveOption(status)
        .toRight(s"Game session status `$status` is invalid... Available are: ${PlayerGameSessionStatus.values}")
    }
  }

  sealed trait RouletteNumberColor extends EnumEntry
  object RouletteNumberColor extends Enum[RouletteNumberColor] {

    val values: IndexedSeq[RouletteNumberColor] = findValues

    final case object Red extends RouletteNumberColor
    final case object Black extends RouletteNumberColor

    def of(color: String): Either[String, RouletteNumberColor] = {
      val option = RouletteNumberColor.withNameInsensitiveOption(color)

      Either.cond(
        option.isDefined,
        option.get,
        s"Color `$color` is invalid... Available colors are: ${RouletteNumberColor.values}"
      )
    }
  }

  sealed abstract case class RouletteNumber private (
    value: Int,
    color: RouletteNumberColor
  )
  object RouletteNumber {
    private val minValue = 1
    private val maxValue = 36

    def of(value: Int, color: String): Either[String, RouletteNumber] = {
      for {
        validatedValue <- Either.cond(
          value >= minValue && value <= maxValue,
          value,
          s"Value `$value` is out of range [$minValue; $maxValue]"
        )

        validatedColor <- RouletteNumberColor.of(color)
      } yield new RouletteNumber(validatedValue, validatedColor) {}
    }
  }

  sealed trait BetType extends EnumEntry
  object BetType extends Enum[BetType] {
    val values: IndexedSeq[BetType] = findValues

    // Inside Bets
    final case object StraightUp extends BetType
    final case object Split extends BetType
    final case object Street extends BetType
    final case object Corner extends BetType
    final case object Line extends BetType

    // Outside Bets
    final case object Column extends BetType
    final case object Dozen extends BetType
    final case object RedOrBlack extends BetType
    final case object EvenOrOdd extends BetType
    final case object LowOrHigh extends BetType
  }

}
