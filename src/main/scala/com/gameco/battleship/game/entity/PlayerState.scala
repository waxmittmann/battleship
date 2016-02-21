package com.gameco.battleship.game.entity

import com.gameco.battleship.entity.{ArrayGrid, Grid, Position}

//case class PlayerState(playerShips: Seq[Ship], playerGrid: Grid[Option[Ship]], attackedPositions: Grid[Boolean]) {
case class PlayerState(width: Int, height: Int, playerShips: Seq[Ship], attackedPositions: Grid[Boolean]) {

  def createGridFromShips(): Grid[Option[Ship]] = {
    val grid = ArrayGrid[Option[Ship]](None, width, height)
    for (ship <- playerShips) {

    }
  }

  val playerGrid: Grid[Option[Ship]] = createGridFromShips()

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
    val hasUnattackedPosition = exists(ship, !playerState.attackedPositions.get(_, _))
    !hasUnattackedPosition

//    for (l <- 0 to ship.size) {
//      if (ship.isHorizontal && !playerState.attackedPositions.get(ship.x + l, ship.y)) {
//        return false
//      } else if (!ship.isHorizontal && !playerState.attackedPositions.get(ship.x, ship.y + l)) {
//        return false
//      }
//    }
//    return true
  }

  private def exists[A](ship: Ship, test: (Int, Int) => Boolean): Boolean = {
    for (l <- 0 to ship.size) {
      if ((ship.isHorizontal && test(ship.x + l, ship.y)) ||
        (!ship.isHorizontal && test(ship.x, ship.y + l))) {
        return true
      }
    }
    return false
  }

  def isAllSunk(): Boolean = {
    for (ship <- playerShips) {
      if (!isShipSunk(ship, this)) {
        return false
      }

//      if (exists(ship, isAttacked(_, _))) {
//        return false
//      }

//      for (l <- 0 to ship.size) {
//        exists(ship, !isAttacked(_, _))
//        if ((ship.isHorizontal && !isAttacked(ship.x + l, ship.y)) ||
//            (!ship.isHorizontal && !isAttacked(ship.x, ship.y + l))) {
//          return false
//        }
//      }
    }
    true
  }
}
