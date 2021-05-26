name := "roulette-server"

version := "0.1"

scalaVersion := "2.13.6"

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-Ymacro-annotations",
  "-Xfatal-warnings",
  "-Xsource:3"
)

ThisBuild / scalafmtOnCompile := true

val http4sVersion           = "0.21.7"
val circeVersion            = "0.13.0"
val circeConfigVersion      = "0.8.0"
val doobieVersion           = "0.9.0"
val catsVersion             = "2.2.0"
val catsTaglessVersion      = "0.11"
val catsEffectVersion       = "2.2.0"
val epimetheusVersion       = "0.4.2"
val catsScalacheckVersion   = "0.2.0"
val log4CatsVersion         = "1.1.1"
val scalaTestVersion        = "3.1.0.0-RC2"
val h2Version               = "1.4.200"
val enumeratumVersion       = "1.6.1"
val dtoMapperChimneyVersion = "0.6.1"

libraryDependencies ++= Seq(
  "org.typelevel"            %% "cats-core"                     % catsVersion,
  "org.typelevel"            %% "cats-effect"                   % catsEffectVersion,
  "org.http4s"               %% "http4s-dsl"                    % http4sVersion,
  "org.http4s"               %% "http4s-blaze-server"           % http4sVersion,
  "org.http4s"               %% "http4s-blaze-client"           % http4sVersion,
  "org.http4s"               %% "http4s-circe"                  % http4sVersion,
  "org.http4s"               %% "http4s-jdk-http-client"        % "0.3.6",
  "io.chrisdavenport"        %% "log4cats-slf4j"                % log4CatsVersion,
  "ch.qos.logback"            % "logback-classic"               % "1.2.3",
  "com.codecommit"           %% "cats-effect-testing-scalatest" % "0.4.1"               % Test,
  "io.chrisdavenport"        %% "epimetheus-http4s"             % epimetheusVersion,
  "io.chrisdavenport"        %% "cats-scalacheck"               % catsScalacheckVersion % Test,
  "org.scalatestplus"        %% "scalatestplus-scalacheck"      % scalaTestVersion      % Test,
  "org.scalatestplus"        %% "selenium-2-45"                 % scalaTestVersion      % Test,
  "org.typelevel"            %% "simulacrum"                    % "1.0.0",
  "org.tpolecat"             %% "atto-core"                     % "0.8.0",
  "io.circe"                 %% "circe-core"                    % circeVersion,
  "io.circe"                 %% "circe-generic"                 % circeVersion,
  "io.circe"                 %% "circe-generic-extras"          % circeVersion,
  "io.circe"                 %% "circe-optics"                  % circeVersion,
  "io.circe"                 %% "circe-parser"                  % circeVersion,
  "org.fusesource.leveldbjni" % "leveldbjni-all"                % "1.8",
  "org.tpolecat"             %% "doobie-core"                   % doobieVersion,
  "org.tpolecat"             %% "doobie-h2"                     % doobieVersion,
  "org.tpolecat"             %% "doobie-hikari"                 % doobieVersion,
  "org.mockito"              %% "mockito-scala"                 % "1.15.0"              % Test,
  "org.scalaj"               %% "scalaj-http"                   % "2.4.2"               % Test,
  "org.tpolecat"             %% "doobie-scalatest"              % doobieVersion         % Test,
  "org.typelevel"            %% "cats-tagless-macros"           % catsTaglessVersion,
  "com.h2database"            % "h2"                            % "1.4.200",
  "eu.timepit"               %% "refined"                       % "0.9.17",
  "org.slf4j"                 % "slf4j-nop"                     % "1.6.4",
  "eu.timepit"               %% "refined"                       % "0.9.21",
  "com.beachape"             %% "enumeratum"                    % enumeratumVersion,
  "io.scalaland"             %% "chimney"                       % dtoMapperChimneyVersion,
  "com.github.pureconfig"    %% "pureconfig"                    % "0.14.0",
  "org.flywaydb"              % "flyway-core"                   % "6.2.4",
  "org.postgresql"            % "postgresql"                    % "42.2.6",
  "io.circe"                 %% "circe-config"                  % circeConfigVersion,
  "io.circe"                 %% "circe-core"                    % circeVersion,
  "io.circe"                 %% "circe-generic"                 % circeVersion,
  "io.circe"                 %% "circe-generic-extras"          % circeVersion,
  "io.circe"                 %% "circe-optics"                  % circeVersion,
  "io.circe"                 %% "circe-parser"                  % circeVersion
)

addCompilerPlugin(
  "org.typelevel" %% "kind-projector" % "0.13.0" cross CrossVersion.full
)

run / fork := true
