package com.roulette.server.service.impl

import cats.effect.Sync
import cats.implicits._
import io.scalaland.chimney.dsl._
import com.roulette.server.dto.game.GameDto
import com.roulette.server.repository.GameRepository
import com.roulette.server.service.RouletteService

class RouletteServiceImpl[F[_]: Sync](gameRepository: GameRepository[F]) extends RouletteService[F] {

  override def findAvailableGames: F[List[GameDto]] = for {
    availableGames <- gameRepository.findAvailableGames
  } yield availableGames.map(_.into[GameDto].withFieldComputed(_.status, _.status.toString).transform)
}
