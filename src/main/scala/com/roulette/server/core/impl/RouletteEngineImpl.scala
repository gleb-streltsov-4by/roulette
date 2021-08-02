package com.roulette.server.core.impl

import cats.effect.Sync
import scala.util.Random
import cats.implicits._

import com.roulette.server.core.RouletteEngine
import com.roulette.server.domain.game.{PlayerGameSession, PlayerGameSessionResult, RouletteNumber}
import com.roulette.server.domain.game.RouletteNumber.{maxNumberValue, minNumberValue}
import com.roulette.server.util.ModelMapper

class RouletteEngineImpl[F[_]: Sync] extends RouletteEngine[F] {

  private val random = new Random(seed = System.currentTimeMillis)

  override def generateNumber: F[RouletteNumber] = for {
    number <- generateRandomNumber
    result  = RouletteNumber(number)
  } yield result

  override def evaluateBets(
    resultNumber: RouletteNumber,
    sessions:     List[PlayerGameSession]
  ): List[PlayerGameSessionResult] = {
    sessions.map(session => {
      val numbers = session.betDetails
      val isWin   = numbers.contains(resultNumber)
      val winRate = session.betType.winRate
      val payoff  = if (isWin) session.betAmount * winRate else -session.betAmount

      ModelMapper.gameSessionToResult(session, resultNumber, isWin, payoff)
    })
  }

  private def generateRandomNumber: F[Int] =
    (minNumberValue + random.nextInt(maxNumberValue - minNumberValue + 1)).pure[F]
}
