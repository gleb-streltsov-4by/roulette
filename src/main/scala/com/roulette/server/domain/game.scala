package com.roulette.server.domain

import enumeratum.{Enum, EnumEntry}

import java.util.UUID

object game {

  final case class Game(gameId: UUID,
                        minBetAmount: Int,
                        maxBetAmount: Int)

  sealed trait GameStatus extends EnumEntry
  object GameStatus extends Enum[GameStatus]{

    val values: IndexedSeq[GameStatus] = findValues

    final case object BetsSubmission extends GameStatus
    final case object InProgress extends GameStatus
    final case object Suspended extends GameStatus
    final case object Archived extends GameStatus

    def of(status: String): Either[String, GameStatus] = {
      val option = GameStatus.withNameInsensitiveOption(status)

      Either.cond(option.isDefined, option.get,
        s"Game status `$status` is invalid... Available are: ${GameStatus.values}")
    }
  }

  final case class Bet(id: UUID,
                       playerId: UUID,
                       gameId: UUID,
                       amount: Int,
                       combination: RouletteCombination)

  sealed trait RouletteNumberColor extends EnumEntry
  object RouletteNumberColor extends Enum[RouletteNumberColor]{

    val values: IndexedSeq[RouletteNumberColor] = findValues

    final case object Red extends RouletteNumberColor
    final case object Black extends RouletteNumberColor

    def of(color: String): Either[String, RouletteNumberColor] = {
      val option = RouletteNumberColor.withNameInsensitiveOption(color)

      Either.cond(option.isDefined, option.get,
        s"Color `$color` is invalid... Available colors are: ${RouletteNumberColor.values}")
    }
  }

  sealed abstract case class RouletteNumber private(value: Int, color: RouletteNumberColor)
  object RouletteNumber {
    private val minValue = 1
    private val maxValue = 36

    def of(value: Int, color: String): Either[String, RouletteNumber] = {
      for {
        validatedValue <- Either.cond(value >= minValue && value <= maxValue, value,
          s"Value `$value` is out of range [$minValue; $maxValue]")

        validatedColor <- RouletteNumberColor.of(color)
      } yield new RouletteNumber(validatedValue, validatedColor) {}
    }
  }

  sealed trait RouletteCombination extends EnumEntry
  object RouletteCombination extends Enum[RouletteCombination] {
    val values: IndexedSeq[RouletteCombination] = findValues

    // Inside Bets
    final case object StraightUp extends RouletteCombination
    final case object Split extends RouletteCombination
    final case object Street extends RouletteCombination
    final case object Corner extends RouletteCombination
    final case object Line extends RouletteCombination

    // Outside Bets
    final case object Column extends RouletteCombination
    final case object Dozen extends RouletteCombination
    final case object RedOrBlack extends RouletteCombination
    final case object EvenOrOdd extends RouletteCombination
    final case object LowOrHigh extends RouletteCombination
  }

}
