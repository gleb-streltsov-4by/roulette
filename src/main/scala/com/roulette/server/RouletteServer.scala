package com.roulette.server

import cats.effect._
import com.roulette.server.repository.GameRepository
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.implicits._
import com.roulette.server.routes.RouletteRoutes
import com.roulette.server.service.RouletteService
import doobie.hikari.HikariTransactor
import doobie.{ExecutionContexts, Transactor}

import scala.concurrent.ExecutionContext

object RouletteServer {

  val port = 9000
  val host = "localhost"

  def configure[F[_]: ContextShift : ConcurrentEffect: Sync](implicit T: Timer[F]): F[Unit] =
    transactor[F]
      .use { tx =>
        val gameRepository = GameRepository.of[F](tx)
        val rouletteService = RouletteService.of[F](gameRepository)

        val httpApp = RouletteRoutes.routes[F](rouletteService).orNotFound

        BlazeServerBuilder[F](ExecutionContext.global)
          .bindHttp(port, host)
          .withHttpApp(httpApp)
          .serve
          .compile
          .drain
      }

  object DbConfig {
    val dbDriverName = "org.h2.Driver"
    val dbUrl        = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
    val dbUser       = ""
    val dbPwd        = ""
  }

  private def transactor[F[_]: ContextShift: Async]: Resource[F, Transactor[F]] =
    for {
      ce <- ExecutionContexts.fixedThreadPool[F](10)
      be <- Blocker[F]
      tx <- HikariTransactor.newHikariTransactor[F](
        driverClassName = DbConfig.dbDriverName,
        url = DbConfig.dbUrl,
        user = DbConfig.dbUser,
        pass = DbConfig.dbPwd,
        connectEC = ce, // await connection on this EC
        blocker = be    // execute JDBC operations on this EC
      )
    } yield tx
}
