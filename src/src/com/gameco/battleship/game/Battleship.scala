package com.gameco.battleship.game

import com.gameco.battleship.entity.{Grid, Position}
import com.gameco.battleship.game.BattleshipGame.ShipPositionOrientation

object BattleshipGame {
  type TurnResult = (TurnResult, BattleshipGame)
  type ShipPositionOrientation = (Position, Boolean)
}

sealed trait ActionError
case object OutsideBounds extends ActionError
case object AlreadyHit extends ActionError
case object GameOver extends ActionError

sealed trait TurnResult
case object Win extends TurnResult
case object Hit extends TurnResult
case object Sunk extends TurnResult
case object Miss extends TurnResult

case class BattleshipGame(width: Int, height: Int, gameState: BattleshipGameState, isPlayerATurn: Boolean) {

  def takeTurn(attackPosition: Position): Either[ActionError, TurnResult] = {
    val (myPlayerState, opposingPlayerState) =
      if (isPlayerATurn) {
        (gameState.playerA, gameState.playerB)
      } else {
        (gameState.playerB, gameState.playerA)
      }
    takeTurnForPlayer(attackPosition, myPlayerState, opposingPlayerState)
  }

  private def takeTurnForPlayer(attackPosition: Position, myPlayerState: PlayerState,
                        opposingPlayerState: PlayerState): Either[ActionError, TurnResult] = {
    if (myPlayerState.playerHits.exists(attackPosition == _)) {
      Left(AlreadyHit)
    } else if (attackPosition.x < 0 || attackPosition.x >= width || attackPosition.y < 0 || attackPosition.y >= height) {
      Left(OutsideBounds)
    } else {
      val hadHit = HitDetection.isHit(attackPosition, opposingPlayerState.playerShips)

      if (hadHit) {
        Right(Hit)
      } else {
        Right(Miss)
      }
    }
  }
  
  def getPlayerPosition(): Grid = ???
  def getOpponentPosition(): Grid = ???

  def isPlayerAWinner(): Option[Boolean] = ???
}
