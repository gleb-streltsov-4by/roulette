package com.roulette.server.service.error

import com.roulette.server.dto.player.PlayerDto

object game {

  sealed trait GameValidationError extends Throwable {
    def message: String
  }

  object GameValidationError {
    final case class InvalidGameBets(minBetAmount: Int, maxBetAmount: Int) extends GameValidationError {
      override def message: String = "Invalid bets are specified. " +
        s"Minimal `$minBetAmount` should be lower than maximum `$maxBetAmount`. "
    }

    final case class InvalidStatus(status: String) extends GameValidationError {
      override def message: String = s"Status `$status` is invalid"
    }

    final case object PlayerCountExceed extends GameValidationError {
      override def message: String = "No available seats in the lobby"
    }

    final case class PlayerBetIsInvalid(players: List[PlayerDto],
                                        minBetAmount: Int,
                                        maxBetAmount: Int) extends GameValidationError {
      override def message: String = {
        val lobbyBets = s"The minimum and maximum bets of the lobby are: `$minBetAmount`, $maxBetAmount."
        val playersBets = players.foldLeft("\n")((acc, player) =>
          s"$acc player `${player.name}` bet is ${player.name}\n")

        lobbyBets + playersBets
      }
    }
  }
}