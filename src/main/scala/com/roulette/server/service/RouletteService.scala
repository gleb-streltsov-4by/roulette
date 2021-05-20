package com.roulette.server.service

import cats.effect.Sync
import com.roulette.server.dto.game.GameDto
import com.roulette.server.repository.GameRepository
import com.roulette.server.service.impl.RouletteServiceImpl

trait RouletteService[F[_]] {
  def findAvailableGames: F[List[GameDto]]
}

object RouletteService {
  def of[F[_]: Sync](gameRepository: GameRepository[F]): RouletteService[F] =
    new RouletteServiceImpl[F](gameRepository)
}
