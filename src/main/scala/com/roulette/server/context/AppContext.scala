package com.roulette.server.context

import cats.effect.{Async, ContextShift, Resource}
import org.http4s.HttpApp
import org.http4s.implicits._

import com.roulette.server.conf.app.AppConf
import com.roulette.server.conf.db.{migrator, transactor}
import com.roulette.server.core.RouletteEngine
import com.roulette.server.repository.GameRepository
import com.roulette.server.routes.RouletteRoutes
import com.roulette.server.service.RouletteService

object AppContext {

  def setUp[F[_]: ContextShift: Async](conf: AppConf): Resource[F, HttpApp[F]] = for {
    tx <- transactor[F](conf.db)

    migrator <- Resource.eval(migrator[F](conf.db))
    _        <- Resource.eval(migrator.migrate())

    gameRepository  = GameRepository.of[F](tx)
    rouletteEngine  = RouletteEngine.of[F]
    rouletteService = RouletteService.of[F](gameRepository, rouletteEngine)

    httpApp = RouletteRoutes.routes[F](rouletteService).orNotFound
  } yield httpApp
}
