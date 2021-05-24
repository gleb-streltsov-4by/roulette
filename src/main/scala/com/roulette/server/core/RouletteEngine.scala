package com.roulette.server.core

import cats.effect.Sync
import com.roulette.server.core.impl.RouletteEngineImpl

trait RouletteEngine[F[_]] {
}

object RouletteEngine {
  def of[F[_]: Sync]: RouletteEngine[F] = new RouletteEngineImpl[F]
}
