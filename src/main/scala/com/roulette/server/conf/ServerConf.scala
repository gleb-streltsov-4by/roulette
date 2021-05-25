package com.roulette.server.conf

import cats.implicits._
import pureconfig.generic.auto._
import cats.effect.Sync
import pureconfig.ConfigSource

object ServerConf {

  private val applicationConfNamespace = "server"

  val port = 9000
  val host = "localhost"

  final case class ServerConf(
      host: String,
      port: Int
  )

  def serverConf[F[_]: Sync]: F[ServerConf] = {
    Sync[F]
      .delay(ConfigSource.default.at(applicationConfNamespace).load[ServerConf])
      .flatMap[ServerConf] { case Right(conf) =>
        conf.pure[F]
      }
  }
}
