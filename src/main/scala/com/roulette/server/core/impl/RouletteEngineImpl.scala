package com.roulette.server.core.impl

import cats.effect.Sync
import com.roulette.server.core.RouletteEngine

class RouletteEngineImpl[F[_]: Sync] extends RouletteEngine[F] {
}
