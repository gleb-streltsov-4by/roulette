package com.roulette.server.core.impl

import cats.effect.Sync
import com.roulette.server.core.RouletteEngine
import com.roulette.server.domain.game
import com.roulette.server.domain.game.{PlayerGameSession, RouletteNumber}

import cats.implicits._

class RouletteEngineImpl[F[_]: Sync] extends RouletteEngine[F] {
  override def generateNumber: F[RouletteNumber] =
    RouletteNumber.of(1, "black").right.get.pure[F]

  override def evaluateBets(
      resultNumber: RouletteNumber,
      sessions: List[game.PlayerGameSession]
  ): F[List[PlayerGameSession]] = List[PlayerGameSession]().pure[F]
}
