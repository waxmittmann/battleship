package com.gameco.battleship.game.entity

import com.gameco.battleship.entity.{Grid, Position}

case class PlayerState(playerShips: Seq[Ship], playerGrid: Grid[Option[Ship]], attackedPositions: Grid[Boolean]) {
  def isAttacked(x: Int, y: Int): Boolean = attackedPositions.get(x, y)

  def setAttacked(attackPosition: Position): (ActionResult, PlayerState) = {
    val newState = this.copy(attackedPositions = attackedPositions.copyWithChange(attackPosition, true))
    val actionResult: ActionResult = playerGrid.get(attackPosition.x, attackPosition.y).fold[ActionResult](Miss)(ship => {
      if (isShipSunk(ship, newState)) {
        Sunk
      } else {
        Hit
      }
    })
    (actionResult, newState)
  }

  private def isShipSunk(ship: Ship, playerState: PlayerState): Boolean = {
    for (l <- 0 to ship.size) {
      if (ship.isHorizontal && !playerState.attackedPositions.get(ship.x + l, ship.y)) {
        return false
      } else if (!ship.isHorizontal && !playerState.attackedPositions.get(ship.x, ship.y + l)) {
        return false
      }
    }
    return true
  }

  def isAllSunk(): Boolean = {
    for (ship <- playerShips) {
      for (l <- 0 to ship.size) {
        if ((ship.isHorizontal && !isAttacked(ship.x + l, ship.y)) ||
            (!ship.isHorizontal && !isAttacked(ship.x, ship.y + l))) {
          return false
        }
      }
    }
    true
  }
}
