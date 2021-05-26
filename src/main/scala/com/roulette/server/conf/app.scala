package com.roulette.server.conf

import io.circe.generic.JsonCodec

object app {

  @JsonCodec
  final case class AppConf(
    server: ServerConf,
    db:     DbConf
  )

  @JsonCodec
  final case class DbConf(
    provider:          String,
    driver:            String,
    url:               String,
    user:              String,
    password:          String,
    migrationLocation: String
  )

  @JsonCodec
  final case class ServerConf(
    host: String,
    port: Int
  )
}
