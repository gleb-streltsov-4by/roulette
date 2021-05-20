package com.roulette.server.routes

import cats.implicits._
import cats.effect.Sync
import com.roulette.server.service.RouletteService
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object RouletteRoutes {

  def routes[F[_] : Sync](rouletteService: RouletteService[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._

    def availableGamesRoutes: HttpRoutes[F] = {
      HttpRoutes.of[F] {
        case GET -> Root / "api" / "roulette" / "game" / "available" => for {
          response <- rouletteService.findAvailableGames
        } yield response
      }
    }

    def joinGamesRoutes: HttpRoutes[F] = {
      HttpRoutes.of[F] {
        case POST -> Root / "api" / "roulette" / "game" / UUIDVar(gameId) => for {
          response <- Ok(s"$gameId")
        } yield response
      }
    }

    availableGamesRoutes <+> joinGamesRoutes
  }
}
