package com.roulette.server

import cats.implicits._
import cats.effect._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.implicits._
import io.circe.config.parser
import com.roulette.server.conf.app._
import com.roulette.server.conf.db._
import com.roulette.server.routes.RouletteRoutes
import com.roulette.server.service.RouletteService
import com.roulette.server.core.RouletteEngine
import com.roulette.server.repository.GameRepository
import org.http4s.server.Server

import scala.concurrent.ExecutionContext

object RouletteServer extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    serverResource[IO]
      .use(_ => IO.never)
      .as(ExitCode.Success)

  private def serverResource[F[_]: ContextShift: ConcurrentEffect: Sync: Timer]: Resource[F, Server[F]] = for {
    conf <- Resource.eval(parser.decodePathF[F, AppConf]("app"))
    tx   <- transactor[F](conf.db)

    migrator <- Resource.eval(migrator[F](conf.db))
    _        <- Resource.eval(migrator.migrate())

    gameRepository  = GameRepository.of[F](tx)
    rouletteEngine  = RouletteEngine.of[F]
    rouletteService = RouletteService.of[F](gameRepository, rouletteEngine)

    httpApp = RouletteRoutes.routes[F](rouletteService).orNotFound

    server <- BlazeServerBuilder[F](ExecutionContext.global)
      .bindHttp(conf.server.port, conf.server.host)
      .withHttpApp(httpApp)
      .resource

  } yield server
}
