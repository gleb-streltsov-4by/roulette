package com.roulette.server.routes

import cats.implicits._
import cats.effect.{MonadThrow, Sync}
import com.roulette.server.dto.game.{GameDto, LeftGameDto, PlayerGameSessionDto}
import com.roulette.server.service.RouletteService
import com.roulette.server.service.error.game.GameValidationError
import com.roulette.server.service.error.game.GameValidationError.{GameNotFound, GameSessionNotFound, PlayerNotFound}
import org.http4s.{EntityEncoder, HttpRoutes, Response}
import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import org.http4s.dsl.Http4sDsl

object RouletteRoutes {

  def routes[F[_]: Sync: MonadThrow](
    rouletteService: RouletteService[F]
  ): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._

    def availableGames: HttpRoutes[F] = HttpRoutes.of[F] { case GET -> Root / "roulette" / "game" / "available" =>
      for {
        games    <- rouletteService.findAvailableGames
        response <- Ok(games)
      } yield response
    }

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

    def joinGame: HttpRoutes[F] = HttpRoutes.of[F] { case req @ POST -> Root / "roulette" / "game" / "join" =>
      val res = for {
        session      <- req.as[PlayerGameSessionDto]
        gameSessions <- rouletteService.addUserToGame(session)
      } yield gameSessions

      marshalResponse(res)
    }

    def leftGame: HttpRoutes[F] = HttpRoutes.of[F] { case req @ DELETE -> Root / "roulette" / "game" / "left" =>
      val res = for {
        leftGame     <- req.as[LeftGameDto]
        gameSessions <- rouletteService.removeUserFromGame(leftGame)
      } yield gameSessions

      marshalResponse(res)
    }

    def gameErrorToHttpResponse(error: GameValidationError): F[Response[F]] = {
      error match {
        case e @ GameNotFound(_)        => NotFound(e.message)
        case e @ GameSessionNotFound(_) => NotFound(e.message)
        case e @ PlayerNotFound(_)      => NotFound(e.message)
        case e @ _                      => BadRequest(e.message)
      }
    }

    def marshalResponse[T](result: F[Either[GameValidationError, T]])(implicit encoder: EntityEncoder[F, T]) = {
      result
        .flatMap {
          case Left(error) => gameErrorToHttpResponse(error)
          case Right(dto)  => Ok(dto)
        }
        .handleErrorWith { ex =>
          InternalServerError(ex.getMessage)
        }
    }

    availableGames <+> updateGame() <+> createGame <+> joinGame <+> leftGame
  }
}
