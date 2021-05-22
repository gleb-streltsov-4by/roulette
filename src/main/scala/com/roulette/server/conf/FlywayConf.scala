package com.roulette.server.conf

import cats.implicits._
import cats.effect.Sync
import com.roulette.server.conf.DbConf.DbConf
import org.flywaydb.core.Flyway

object FlywayConf {

  private class FlywayMigrator[F[_]: Sync](dbConf: DbConf) {
    def migrate(): F[Int] =
      for {
        conf <- migrationConfig(dbConf)
        res <- Sync[F].delay(conf.migrate())
      } yield res

    private def migrationConfig(dbConf: DbConf): F[Flyway] = {
      Sync[F].delay(
        Flyway
          .configure()
          .dataSource(dbConf.url, dbConf.user, dbConf.password)
          .locations(dbConf.migrationLocation)
          .load()
      )
    }
  }

  def migrate[F[_]: Sync](dbConf: DbConf): F[Int] =
    new FlywayMigrator[F](dbConf).migrate()

}
