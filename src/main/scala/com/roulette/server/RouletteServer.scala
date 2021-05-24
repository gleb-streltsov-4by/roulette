package com.roulette.server

import cats.implicits._
import cats.effect._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.implicits._
import com.roulette.server.routes.RouletteRoutes
import com.roulette.server.service.RouletteService
import com.roulette.server.conf.DbConf._
import com.roulette.server.conf.FlywayConf._
import com.roulette.server.core.RouletteEngine
import com.roulette.server.repository.GameRepository

import scala.concurrent.ExecutionContext

object RouletteServer {

  val port = 9000
  val host = "localhost"

  def configure[F[_]: ContextShift : ConcurrentEffect: Sync](implicit T: Timer[F]): F[Unit] = {
    val resourceF = for {
      dbConf  <- dbConf[F]
      _       <- migrate(dbConf)
      tx = transactor[F](dbConf)
    } yield tx

    resourceF.flatMap(_.use { tx =>
      val gameRepository = GameRepository.of[F](tx)
      val rouletteEngine = RouletteEngine.of[F]
      val rouletteService = RouletteService.of[F](gameRepository, rouletteEngine)

      val httpApp = RouletteRoutes.routes[F](rouletteService).orNotFound

      BlazeServerBuilder[F](ExecutionContext.global)
        .bindHttp(port, host)
        .withHttpApp(httpApp)
        .serve
        .compile
        .drain
    })

  }
}
