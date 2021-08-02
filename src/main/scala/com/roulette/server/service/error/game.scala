package com.roulette.server.service.error

import scala.util.control.NoStackTrace

import com.roulette.server.dto.game.{GameDto, PlayerGameSessionDto}
import com.roulette.server.dto.player.PlayerDto

object game {

  sealed trait GameValidationError extends RuntimeException with NoStackTrace {
    def message: String
  }

  object GameValidationError {
    // 404
    final case class GameNotFound(game: GameDto) extends GameValidationError {
      override def message: String =
        s"The game with id `${game.id}` is not found"
    }

    // 404
    final case class GameSessionNotFound(session: PlayerGameSessionDto) extends GameValidationError {
      override def message: String =
        s"The game session with id `${session.id}` of game with id `${session.gameId}` is not found"
    }

    // 404
    final case class PlayerNotFound(playerId: Int) extends GameValidationError {
      override def message: String =
        s"The player with id `$playerId` is not found"
    }

    // 403
    final case class PlayerIsNotHost(playerId: Int, gameId: Int) extends GameValidationError {
      override def message: String =
        s"The player with id `$playerId` isn't a host of game with id `$gameId``"
    }

    // 400
    final case class InvalidGameBets(game: GameDto) extends GameValidationError {
      override def message: String = "Invalid bets are specified. " +
        s"Minimal `${game.minBetAmount}` should be lower than maximum `${game.maxBetAmount}`."
    }

    // 400
    final case class InvalidGameStatus(game: GameDto) extends GameValidationError {
      override def message: String = s"Status `${game.status}` is invalid."
    }

    // 400
    final case object PlayerCountExceed extends GameValidationError {
      override def message: String = "No available seats in the lobby."
    }

    // 400
    final case class PlayerBetIsInvalid(
      players:      List[PlayerDto],
      minBetAmount: Int,
      maxBetAmount: Int
    ) extends GameValidationError {
      override def message: String = {
        val lobbyBets =
          s"The minimum and maximum bets of the lobby are: `$minBetAmount`, $maxBetAmount."
        val playersBets =
          players.foldLeft("\n")((acc, player) => s"$acc player `${player.name}` bet is ${player.name}\n")

        lobbyBets + playersBets
      }
    }

    // 500
    final case class TechnicalError(cause: String) extends GameValidationError {
      override def message: String = s"Technical error is occurred. Details: $cause"
    }
  }
}
