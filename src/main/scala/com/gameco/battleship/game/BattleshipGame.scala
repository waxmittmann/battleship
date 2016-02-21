package com.gameco.battleship.game

import com.gameco.battleship.entity.{Grid, Position}
import com.gameco.battleship.game.BattleshipGame.ResultAndNewState
import com.gameco.battleship.game.entity._
import com.gameco.battleship.game.helpers.PlayerPositionViewCreator

object BattleshipGame {
  type ShipPositionOrientation = (Position, Boolean)
  type ResultAndNewState = (ActionResult, BattleshipGame)

  def create(width: Int, height: Int, gameState: BattleshipGameState): BattleshipGame = {
    BattleshipGame(gameState, true, None)
  }
}

case class BattleshipGame(gameState: BattleshipGameState, isPlayerATurn: Boolean, isPlayerAWinner: Option[Boolean]) {

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
      } else if (attackPosition.x < 0 || attackPosition.x >= gameState.width || attackPosition.y < 0 || attackPosition.y >= gameState.height) {
        Some(OutsideBounds)
      } else if (opposingPlayerState.isAttacked(attackPosition.x, attackPosition.y)) {
        Some(AlreadyHit)
      } else {
        None
      }
    }

    isMoveError.fold[Either[ActionError, ResultAndNewState]] {
      takeTurn(attackPosition, opposingPlayerState, playerStateWriter)
    } {
      error => Left(error)
    }
  }

  private def takeTurn(attackPosition: Position, opposingPlayerState: PlayerState,
                           playerStateWriter: (PlayerState) => BattleshipGameState): Right[Nothing, (ActionResult, BattleshipGame)] = {
    val result: (ActionResult, PlayerState) = opposingPlayerState.setAttacked(attackPosition)
    val newState: BattleshipGameState = playerStateWriter(result._2)
    val isPlayerAWinner: Option[Boolean] = if (result._2.isAllSunk()) {
        Some(isPlayerATurn)
      } else {
        None
      }

    Right((result._1, this.copy(gameState = newState, isPlayerATurn = !isPlayerATurn, isPlayerAWinner = isPlayerAWinner)))
  }

  def getPlayerPosition(): Grid[GridStatus] = {
    PlayerPositionViewCreator.getPosition(gameState.width, gameState.height, getCurrentPlayerState, false)
  }

  def getOpponentPosition(): Grid[GridStatus] = {
    PlayerPositionViewCreator.getPosition(gameState.width, gameState.height, getCurrentOpponentState, true)
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
