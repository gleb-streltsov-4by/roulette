package com.roulette.server

import cats.effect._
import org.http4s.server.blaze.BlazeServerBuilder
import io.circe.config.parser
import com.roulette.server.conf.app._
import com.roulette.server.context.AppContext
import org.http4s.server.Server

import scala.concurrent.ExecutionContext

object RouletteServer extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    serverResource[IO]
      .use(_ => IO.never)
      .as(ExitCode.Success)

  private def serverResource[F[_]: ContextShift: ConcurrentEffect: Timer]: Resource[F, Server[F]] = for {
    conf    <- Resource.eval(parser.decodePathF[F, AppConf]("app"))
    httpApp <- AppContext.setUp[F](conf)

    server <- BlazeServerBuilder[F](ExecutionContext.global)
      .bindHttp(conf.server.port, conf.server.host)
      .withHttpApp(httpApp)
      .resource

  } yield server
}
