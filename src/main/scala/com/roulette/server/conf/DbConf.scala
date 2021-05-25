package com.roulette.server.conf

import cats.implicits._
import cats.effect.{Async, Blocker, ContextShift, Resource, Sync}
import pureconfig.generic.auto._
import pureconfig.ConfigSource
import doobie.hikari.HikariTransactor
import doobie.{ExecutionContexts, Transactor}

object DbConf {

  private val applicationConfNamespace = "db"

  final case class DbConf(
      provider: String,
      driver: String,
      url: String,
      user: String,
      password: String,
      migrationLocation: String
  )

  def dbConf[F[_]: Sync]: F[DbConf] = {
    Sync[F]
      .delay(ConfigSource.default.at(applicationConfNamespace).load[DbConf])
      .flatMap[DbConf] { case Right(conf) =>
        conf.pure[F]
      }
  }

  def transactor[F[_]: ContextShift: Async](
      dbConf: DbConf
  ): Resource[F, Transactor[F]] = for {
    ce <- ExecutionContexts.fixedThreadPool[F](10)
    be <- Blocker[F]
    tx <- HikariTransactor.newHikariTransactor[F](
      driverClassName = dbConf.driver,
      url = dbConf.url,
      user = dbConf.user,
      pass = dbConf.password,
      connectEC = ce, // await connection on this EC
      blocker = be // execute JDBC operations on this EC
    )
  } yield tx
}
