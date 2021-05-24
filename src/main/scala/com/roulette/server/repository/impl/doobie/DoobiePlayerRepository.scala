package com.roulette.server.repository.impl.doobie

import cats.effect.{Bracket, Sync}
import doobie.implicits._
import doobie.{Fragment, Transactor}
import com.roulette.server.domain.player.Player
import com.roulette.server.repository.PlayerRepository

class DoobiePlayerRepository[F[_]: Sync](tx: Transactor[F])(implicit
    ev: Bracket[F, Throwable]
) extends PlayerRepository[F] {

  private val players: Fragment = fr"SELECT * FROM player"

  override def findById(playerId: Int): F[Option[Player]] =
    (players ++ fr"WHERE id = $playerId").query[Player].option.transact(tx)

  override def findAll: F[List[Player]] =
    players.query[Player].to[List].transact(tx)
}
