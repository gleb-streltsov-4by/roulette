package com.roulette.server.domain

import enumeratum.{CirceEnum, Enum, EnumEntry}

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
  object GameStatus extends Enum[GameStatus] with CirceEnum[GameStatus] {

    val values: IndexedSeq[GameStatus] = findValues

    final case object Announced extends GameStatus
    final case object BetSubmission extends GameStatus
    final case object InProgress extends GameStatus
    final case object Suspended extends GameStatus
    final case object Archived extends GameStatus
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
  )

  sealed trait PlayerGameSessionStatus extends EnumEntry
  object PlayerGameSessionStatus extends Enum[PlayerGameSessionStatus] with CirceEnum[PlayerGameSessionStatus] {

    val values: IndexedSeq[PlayerGameSessionStatus] = findValues

    final case object ACTIVE extends PlayerGameSessionStatus
    final case object INACTIVE extends PlayerGameSessionStatus
  }

  sealed trait RouletteNumberColor extends EnumEntry
  object RouletteNumberColor extends Enum[RouletteNumberColor] with CirceEnum[RouletteNumberColor] {

    val values: IndexedSeq[RouletteNumberColor] = findValues

    final case object Red extends RouletteNumberColor
    final case object Black extends RouletteNumberColor
  }

  final case class RouletteNumber(value: Int)
  object RouletteNumber {
    val minNumberValue = 1
    val maxNumberValue = 36
  }

  sealed trait BetType extends EnumEntry

  object BetType extends Enum[BetType] with CirceEnum[BetType] {
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

  object BetUtils {
    def winRate(bet: BetType): Int = {
      bet match {
        case BetType.StraightUp => 36
        case BetType.Split      => 18
        case BetType.Street     => 12
        case BetType.Corner     => 9
        case BetType.Line       => 6
        case BetType.Dozen      => 3
        case BetType.Column     => 3
        case BetType.RedOrBlack => 2
        case BetType.EvenOrOdd  => 2
        case BetType.LowOrHigh  => 2
      }
    }
  }

  final case class PlayerGameSessionResult(
    id:           Int,
    playerId:     Int,
    sessionId:    Int,
    resultNumber: RouletteNumber,
    betType:      BetType,
    isWin:        Boolean,
    payoff:       Int,
  )
}
