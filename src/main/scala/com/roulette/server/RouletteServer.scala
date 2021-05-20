package com.roulette.server

import fs2.Stream
import cats.implicits._
import cats.effect._
import doobie.implicits._
import org.http4s.dsl.io._
import org.http4s.implicits._
import cats.effect.{Async, Blocker, ConcurrentEffect, ContextShift, ExitCode, Resource, Sync, Timer}
import com.roulette.server.repository.GameRepository
import com.roulette.server.repository.impl.DoobieGameRepository
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.implicits._
import com.roulette.server.routes.RouletteRoutes
import com.roulette.server.service.RouletteService
import com.roulette.server.service.impl.RouletteServiceImpl
import doobie.hikari.HikariTransactor
import doobie.{ExecutionContexts, Transactor}

import scala.concurrent.ExecutionContext.global

object RouletteServer {

  val port = 9000
  val host = "localhost"

  def configure[F[_]: ConcurrentEffect](implicit T: Timer[F]): Stream[F, ExitCode] = {
    db[F]
      .use { tx =>
        val gameRepository = GameRepository.of[F](tx)
        val rouletteService = RouletteService.of[F](gameRepository)

        val httpApp = RouletteRoutes.routes[F](rouletteService).orNotFound

        BlazeServerBuilder[F](global)
          .bindHttp(port, host)
          .withHttpApp(httpApp)
          .serve
      }
      .as(ExitCode.Success)
  }

  object DbConfig {
    val dbDriverName = "org.h2.Driver"
    val dbUrl        = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
    val dbUser       = ""
    val dbPwd        = ""
  }

  private def db[F[_]: ContextShift: Async]: Resource[F, Transactor[F]] =
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
