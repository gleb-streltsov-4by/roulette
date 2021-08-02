package com.roulette.server.routes

import cats.implicits._
import cats.effect.Sync
import org.http4s.{EntityEncoder, HttpRoutes, Response}
import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import org.http4s.dsl.Http4sDsl

import com.roulette.server.dto.game.{GameDto, GameSessionChangeDto}
import com.roulette.server.service.RouletteService
import com.roulette.server.service.error.game.GameValidationError
import com.roulette.server.service.error.game.GameValidationError.{
  GameNotFound,
  GameSessionNotFound,
  PlayerIsNotHost,
  PlayerNotFound
}

object RouletteRoutes {

  def routes[F[_]: Sync](rouletteService: RouletteService[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._

    def updateGame(): HttpRoutes[F] = HttpRoutes.of[F] { case req @ PUT -> Root / "roulette" / "game" =>
      val res = for {
        game    <- req.as[GameDto]
        updated <- rouletteService.updateGame(game)
      } yield updated

      marshalResponse(res)
    }

    def createGame: HttpRoutes[F] = HttpRoutes.of[F] { case req @ POST -> Root / "roulette" / "game" =>
      val res = for {
        game    <- req.as[GameDto]
        created <- rouletteService.createGame(game)
      } yield created

      marshalResponse(res)
    }

    def availableGames: HttpRoutes[F] = HttpRoutes.of[F] { case GET -> Root / "roulette" / "game" / "available" =>
      for {
        games    <- rouletteService.findAvailableGames
        response <- Ok(games)
      } yield response
    }

    def startGame: HttpRoutes[F] = HttpRoutes.of[F] { case req @ POST -> Root / "roulette" / "game" / "start" =>
      for {
        change      <- req.as[GameSessionChangeDto]
        gameResults <- Ok(rouletteService.startGame(change))
      } yield gameResults
    }

    def gameErrorToHttpResponse(error: GameValidationError): F[Response[F]] =
      error match {
        case e: GameNotFound        => NotFound(e.message)
        case e: GameSessionNotFound => NotFound(e.message)
        case e: PlayerNotFound      => NotFound(e.message)
        case e: PlayerIsNotHost     => Forbidden(e.message)

        case e => BadRequest(e.message)
      }

    def marshalResponse[T](
      result: F[Either[GameValidationError, T]]
    )(
      implicit E: EntityEncoder[F, T]
    ): F[Response[F]] =
      result
        .flatMap {
          case Left(error) => gameErrorToHttpResponse(error)
          case Right(dto)  => Ok(dto)
        }
        .handleErrorWith { ex =>
          InternalServerError(ex.getMessage)
        }

    availableGames <+> updateGame() <+> createGame <+> startGame
  }
}
