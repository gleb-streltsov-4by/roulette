package com.roulette.server

import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    RouletteServer.configure[IO]
      .compile
      .drain
      .as(ExitCode.Success)
}
