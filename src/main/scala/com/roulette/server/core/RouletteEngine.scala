package com.roulette.server.core

import cats.effect.Sync
import com.roulette.server.core.impl.RouletteEngineImpl
import com.roulette.server.domain.game.{PlayerGameSession, PlayerGameSessionResult, RouletteNumber}

trait RouletteEngine[F[_]] {
  def generateNumber: F[RouletteNumber]

  def evaluateBets(
    resultNumber: RouletteNumber,
    sessions:     List[PlayerGameSession]
  ): List[PlayerGameSessionResult]
}

object RouletteEngine {
  def of[F[_]: Sync]: RouletteEngine[F] = new RouletteEngineImpl[F]
}
