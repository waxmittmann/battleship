package com.gameco.battleship.game

import com.gameco.battleship.entity.{ArrayGrid, Grid, Position}
import com.gameco.battleship.game.BattleshipGame.{ResultAndNewState, ShipPositionOrientation}

object BattleshipGame {
  type ShipPositionOrientation = (Position, Boolean)
  type ResultAndNewState = (ActionResult, BattleshipGame)
}

case class BattleshipGame(width: Int, height: Int, gameState: BattleshipGameState, isPlayerATurn: Boolean, isPlayerAWinner: Option[Boolean]) {

  def takeTurn(attackPosition: Position): Either[ActionError, ResultAndNewState] = {
    val (newStateCreator, opposingPlayerState) =
      if (isPlayerATurn) {
        val stateCreator = (playerState: PlayerState) => gameState.copy(playerB = playerState)
        (stateCreator, gameState.playerB)
      } else {
        val stateCreator = (playerState: PlayerState) => gameState.copy(playerA = playerState)
        (stateCreator, gameState.playerA)
      }
    takeTurnForPlayer(attackPosition, opposingPlayerState, newStateCreator)
  }

  private def takeTurnForPlayer(attackPosition: Position, opposingPlayerState: PlayerState,
                                playerStateWriter: PlayerState => BattleshipGameState): Either[ActionError, ResultAndNewState] = {
    if (isPlayerAWinner.isDefined) {
      Left(GameOver)
    } else if (attackPosition.x < 0 || attackPosition.x >= width || attackPosition.y < 0 || attackPosition.y >= height) {
      Left(OutsideBounds)
    } else if (opposingPlayerState.isAttacked.get(attackPosition.x, attackPosition.y)) {
      Left(AlreadyHit)
    } else {
      val result: (ActionResult, PlayerState) = opposingPlayerState.setAttacked(attackPosition)
      val newState: BattleshipGameState = playerStateWriter(result._2)
      Right((result._1, this.copy(gameState = newState)))
    }
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
