package com.gameco.battleship.game

import com.gameco.battleship.entity.{ArrayGrid, Grid, Position}
import com.gameco.battleship.game.BattleshipGame.ShipPositionOrientation

object BattleshipGame {
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

sealed trait GridStatus
case object LocationNotHit extends GridStatus
case object LocationMiss extends GridStatus
case object LocationHit extends GridStatus
case object LocationSunk extends GridStatus

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
    if (attackPosition.x < 0 || attackPosition.x >= width || attackPosition.y < 0 || attackPosition.y >= height) {
      Left(OutsideBounds)
    } else {
      val statusAtAttackPosition: GridStatus = opposingPlayerState.playerStatus.get(attackPosition.x, attackPosition.y)

      statusAtAttackPosition match {
        case LocationHit => Left(AlreadyHit)
        case LocationSunk => Left(AlreadyHit)
        case LocationMiss => Left(AlreadyHit)
        case LocationNotHit => Right({
          if (opposingPlayerState.playerStatus.get(attackPosition.x, attackPosition.y))

          val hadHit = HitDetection.isHit(attackPosition, opposingPlayerState.playerStatus)

          if (hadHit) {
            Right(Hit)
          } else {
            Right(Miss)
          }
        })
      }

    }
  }

  def getPlayerPosition(): Grid[GridStatus] = ???
  def getOpponentPosition(): Grid[GridStatus] = ???

  private def getPositionForPlayer(myPlayerState: PlayerState, opposingPlayerState: PlayerState): Grid[GridStatus] = {
    val result: ArrayGrid[GridStatus] = ArrayGrid(LocationNotHit, width, height)

    for (hit <- opposingPlayerState.playerHits) {
      result.set(hit.x, hit.y)
    }

  }

    def isPlayerAWinner(): Option[Boolean] = ???
}
