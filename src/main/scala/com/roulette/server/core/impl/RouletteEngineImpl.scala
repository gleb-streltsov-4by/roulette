package com.roulette.server.core.impl

import cats.effect.Sync
import com.roulette.server.core.RouletteEngine
import com.roulette.server.domain.game
import com.roulette.server.domain.game.RouletteNumber

class RouletteEngineImpl[F[_]: Sync] extends RouletteEngine[F] {

  override def generateNumber: F[RouletteNumber] = ???

  override def evaluateBets(
      sessions: List[game.PlayerGameSession]
  ): List[game.PlayerGameSession] = ???
}
