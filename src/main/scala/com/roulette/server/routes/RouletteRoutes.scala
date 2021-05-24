package com.roulette.server.routes

import cats.implicits._
import cats.effect.Sync
import com.roulette.server.dto.game.{GameDto, LeftGameDto, PlayerGameSessionDto}
import com.roulette.server.service.RouletteService
import com.roulette.server.service.error.game.GameValidationError
import org.http4s.{HttpRoutes, Response}
import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import org.http4s.dsl.Http4sDsl

object RouletteRoutes {

  def routes[F[_]: Sync](rouletteService: RouletteService[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._

    def availableGames: HttpRoutes[F] =
      HttpRoutes.of[F] {
        case GET -> Root / "api" / "roulette" / "game" / "available" => for {
          games <- rouletteService.findAvailableGames
          response <- Ok(games)
        } yield response
      }

    def updateGame: HttpRoutes[F] =
      HttpRoutes.of[F] {
        case request @ PUT -> Root / "api" / "roulette" / "game" =>
          (for {
            game    <- request.as[GameDto]
            updated <- rouletteService.updateGame(game)

            response = updated match {
              case Left(error)  => gameErrorToHttpResponse(error)
              case Right(dto)   => Ok(dto)
            }
          } yield response).flatten
      }

    def createGame: HttpRoutes[F] =
      HttpRoutes.of[F] {
        case request @ POST -> Root / "api" / "roulette" / "game" =>
          (for {
            game    <- request.as[GameDto]
            created <- rouletteService.createGame(game)

            response = created match {
              case Left(error)  => gameErrorToHttpResponse(error)
              case Right(dto)   => Ok(dto)
            }
          } yield response).flatten
      }

    def joinGame: HttpRoutes[F] =
      HttpRoutes.of[F] {
        case request @ POST -> Root / "api" / "roulette" / "game" / "join" => {
          (for {
            session       <- request.as[PlayerGameSessionDto]
            gameSessions  <- rouletteService.addUserToGame(session)

            response = gameSessions match {
              case Left(error)  => gameErrorToHttpResponse(error)
              case Right(dto)   => Ok(dto)
            }
          } yield response).flatten
        }
      }

    def leftGame: HttpRoutes[F] =
      HttpRoutes.of[F] {
        case request @ DELETE -> Root / "api" / "roulette" / "game" / "left" => {
          (for {
            leftGame      <- request.as[LeftGameDto]
            gameSessions  <- rouletteService.removeUserFromGame(leftGame)

            response = gameSessions match {
              case Left(error)  => gameErrorToHttpResponse(error)
              case Right(dto)   => Ok(dto)
            }
          } yield response).flatten
        }
      }

    def gameErrorToHttpResponse(error: GameValidationError): F[Response[F]] = {
      error match {
        case e @ _ => BadRequest(e.message)
      }
    }

    availableGames <+> updateGame <+> createGame <+> joinGame <+> leftGame
  }
}
