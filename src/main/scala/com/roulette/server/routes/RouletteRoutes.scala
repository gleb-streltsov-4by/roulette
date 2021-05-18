package com.roulette.server.routes

import cats.implicits._
import cats.effect.Sync

import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object RouletteRoutes {

  def routes[F[_] : Sync](): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._

    def testRoutes: HttpRoutes[F] = {
      HttpRoutes.of[F] {
        case GET -> Root / "api" / "roulette" / "test" => for {
          response <- Ok("test")
        } yield response
      }
    }

    testRoutes
  }
}
