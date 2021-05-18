package com.roulette.server

import fs2.Stream
import cats.effect.{ConcurrentEffect, Timer}
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.implicits._

import com.roulette.server.routes.RouletteRoutes

import scala.concurrent.ExecutionContext.global

object RouletteServer {

  val port = 9000
  val host = "localhost"

  def stream[F[_]: ConcurrentEffect](implicit T: Timer[F]): Stream[F, Nothing] = {

    val httpApp = RouletteRoutes.routes[F]().orNotFound

    for {
      exitCode <- BlazeServerBuilder[F](global)
        .bindHttp(port, host)
        .withHttpApp(httpApp)
        .serve
    } yield exitCode

  }.drain

}
