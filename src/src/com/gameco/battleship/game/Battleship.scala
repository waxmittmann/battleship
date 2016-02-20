package com.gameco.battleship.game

import com.gameco.battleship.entity.{ArrayGrid, Grid, Position}
import com.gameco.battleship.game.BattleshipGame.{ResultAndNewState, ShipPositionOrientation}

object BattleshipGame {
  type ShipPositionOrientation = (Position, Boolean)
  type ResultAndNewState = (ActionResult, BattleshipGame)
}

sealed trait ActionError
case object OutsideBounds extends ActionError
case object AlreadyHit extends ActionError
case object GameOver extends ActionError

sealed trait ActionResult
case object Win extends ActionResult
case object Hit extends ActionResult
case object Sunk extends ActionResult
case object Miss extends ActionResult

sealed trait GridStatus
case object UndamagedShipMidsection extends GridStatus
case object UndamagedShipEnd extends GridStatus
case object DamagedShip extends GridStatus
case object SunkShip extends GridStatus
case object AttackedSea extends GridStatus
case object Sea extends GridStatus

case class BattleshipGame(width: Int, height: Int, gameState: BattleshipGameState, isPlayerATurn: Boolean) {

  def takeTurn(attackPosition: Position): Either[ActionError, ActionResult] = {
    val (myPlayerState, opposingPlayerState) =
      if (isPlayerATurn) {
        (gameState.playerA, gameState.playerB)
      } else {
        (gameState.playerB, gameState.playerA)
      }
    takeTurnForPlayer(attackPosition, myPlayerState, opposingPlayerState)
  }

  private def takeTurnForPlayer(attackPosition: Position, myPlayerState: PlayerState,
                        opposingPlayerState: PlayerState): Either[ActionError, ResultAndNewState] = {
    if (attackPosition.x < 0 || attackPosition.x >= width || attackPosition.y < 0 || attackPosition.y >= height) {
      Left(OutsideBounds)
    } else {
      val statusAtAttackPosition: GridStatus = opposingPlayerState.playerStatus.get(attackPosition.x, attackPosition.y)

      val result = statusAtAttackPosition match {
        case (DamagedShip | AttackedSea | SunkShip) => Left(AlreadyHit)
        case (UndamagedShipMidsection | UndamagedShipEnd | Sea) => Right(Hit, createResult(attackPosition))
      }

      result
    }
  }

  def createResult(attackPosition: Position) = {
    //Todo: Having to know this again sucks.
    if (isPlayerATurn) {
      this.copy(gameState = gameState.copy(playerB = gameState.playerB.hitLocation(attackPosition)), isPlayerATurn = !isPlayerATurn)
    } else {
      this.copy(gameState = gameState.copy(playerA = gameState.playerA.hitLocation(attackPosition)), isPlayerATurn = !isPlayerATurn)
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
