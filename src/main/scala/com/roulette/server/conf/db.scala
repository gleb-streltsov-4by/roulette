package com.roulette.server.conf

import cats.implicits._
import cats.effect.{Async, Blocker, ContextShift, Resource, Sync}
import doobie.Transactor
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import org.flywaydb.core.Flyway

import com.roulette.server.conf.app.DbConf

object db {

  def transactor[F[_]: ContextShift: Async](
    dbConf: DbConf
  ): Resource[F, Transactor[F]] = for {
    ce <- ExecutionContexts.fixedThreadPool[F](10)
    be <- Blocker[F]
    tx <- HikariTransactor.newHikariTransactor[F](
      driverClassName = dbConf.driver,
      url             = dbConf.url,
      user            = dbConf.user,
      pass            = dbConf.password,
      connectEC       = ce,
      blocker         = be
    )
  } yield tx

  class FlywayMigrator[F[_]: Sync](dbConf: DbConf) {
    def migrate(): F[Int] =
      for {
        conf <- migrationConfig(dbConf)
        res  <- Sync[F].delay(conf.migrate())
      } yield res

    private def migrationConfig(dbConf: DbConf): F[Flyway] = {
      Sync[F].delay(
        Flyway
          .configure()
          .dataSource(dbConf.url, dbConf.user, dbConf.password)
          .locations(s"${dbConf.migrationLocation}/${dbConf.provider}")
          .load()
      )
    }
  }

  def migrator[F[_]: Sync](dbConf: DbConf): F[FlywayMigrator[F]] =
    new FlywayMigrator[F](dbConf).pure[F]
}
