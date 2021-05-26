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

  sealed trait BetType extends EnumEntry {
    def winRate: Int
  }
  object BetType extends Enum[BetType] with CirceEnum[BetType] {
    val values: IndexedSeq[BetType] = findValues

    // Inside Bets
    final case object StraightUp extends BetType {
      override def winRate: Int = 35
    }
    final case object Split extends BetType {
      override def winRate: Int = 17
    }
    final case object Street extends BetType {
      override def winRate: Int = 11
    }
    final case object Corner extends BetType {
      override def winRate: Int = 8
    }
    final case object Line extends BetType {
      override def winRate: Int = 5
    }

    // Outside Bets
    final case object Column extends BetType {
      override def winRate: Int = 2
    }
    final case object Dozen extends BetType {
      override def winRate: Int = 2
    }
    final case object RedOrBlack extends BetType {
      override def winRate: Int = 1
    }
    final case object EvenOrOdd extends BetType {
      override def winRate: Int = 1
    }
    final case object LowOrHigh extends BetType {
      override def winRate: Int = 1
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
