package com.gameco.battleship.game

import com.gameco.battleship.entity.{ArrayGrid, Grid, Position}
import com.gameco.battleship.game.entity._
import com.gameco.battleship.game.BattleshipGame.{ResultAndNewState, ShipPositionOrientation}
import com.gameco.battleship.game.entity._

object BattleshipGame {
  type ShipPositionOrientation = (Position, Boolean)
  type ResultAndNewState = (ActionResult, BattleshipGame)
}

case class BattleshipGame(width: Int, height: Int, gameState: BattleshipGameState, isPlayerATurn: Boolean, isPlayerAWinner: Option[Boolean]) {

  def takeTurnForCurrentPlayer(attackPosition: Position): Either[ActionError, ResultAndNewState] = {
    val (newStateCreator, opposingPlayerState) =
      if (isPlayerATurn) {
        val stateCreator = (playerState: PlayerState) => gameState.copy(playerB = playerState)
        (stateCreator, gameState.playerB)
      } else {
        val stateCreator = (playerState: PlayerState) => gameState.copy(playerA = playerState)
        (stateCreator, gameState.playerA)
      }
    attemptToTakeTurn(attackPosition, opposingPlayerState, newStateCreator)
  }

  private def attemptToTakeTurn(attackPosition: Position, opposingPlayerState: PlayerState,
                                playerStateWriter: PlayerState => BattleshipGameState): Either[ActionError, ResultAndNewState] = {
    def isMoveError: Option[ActionError] = {
      if (isPlayerAWinner.isDefined) {
        Some(GameOver)
      } else if (attackPosition.x < 0 || attackPosition.x >= width || attackPosition.y < 0 || attackPosition.y >= height) {
        Some(OutsideBounds)
      } else if (opposingPlayerState.isAttacked.get(attackPosition.x, attackPosition.y)) {
        Some(AlreadyHit)
      } else {
        None
      }
    }

    isMoveError.fold[Either[ActionError, ResultAndNewState]] {
      takeTurnForCurrentPlayer(attackPosition, opposingPlayerState, playerStateWriter)
    } {
      error => Left(error)
    }
  }

  private def takeTurnForCurrentPlayer(attackPosition: Position, opposingPlayerState: PlayerState,
                           playerStateWriter: (PlayerState) => BattleshipGameState): Right[Nothing, (ActionResult, BattleshipGame)] = {
    val result: (ActionResult, PlayerState) = opposingPlayerState.setAttacked(attackPosition)
    val newState: BattleshipGameState = playerStateWriter(result._2)
    Right((result._1, this.copy(gameState = newState)))
  }

  def getPlayerPosition(): Grid[GridStatus] = {
    getPosition(getCurrentPlayerState, false)
  }

  def getOpponentPosition(): Grid[GridStatus] = {
    getPosition(getCurrentOpponentState, true)
  }

  private def getPosition(playerState: PlayerState, hideUndamagedShips: Boolean): Grid[GridStatus] = {
    val playerPosition = ArrayGrid[GridStatus](Sea, width, height)

    for {
      y <- 0 until height
      x <- 0 until width
    } yield {
      if (playerState.isAttacked.get(x, y)) {
        if (playerState.playerShips.get(x, y).isDefined) {
          playerPosition.set(x, y, DamagedShip)
        } else {
          playerPosition.set(x, y, AttackedSea)
        }
      } else {
        if (!hideUndamagedShips && playerState.playerShips.get(x, y).isDefined) {
          playerPosition.set(x, y, UndamagedShip)
        } else {
          playerPosition.set(x, y, Sea)
        }
      }
    }

    playerPosition
  }

  private def getCurrentPlayerState: PlayerState = {
    if (isPlayerATurn) {
      gameState.playerA
    } else {
      gameState.playerB
    }
  }

  private def getCurrentOpponentState: PlayerState = {
    if (isPlayerATurn) {
      gameState.playerB
    } else {
      gameState.playerA
    }
  }
}
