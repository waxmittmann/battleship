package com.gameco.battleship.game.entity

import com.gameco.battleship.entity.{ArrayGrid, Grid, Position}

case class OverlappingShipsException(message: String) extends RuntimeException
case class OutOfBoundsShipException(message: String) extends RuntimeException

case class PlayerState(width: Int, height: Int, playerShips: List[Ship], attackedPositions: Grid[Boolean]) {
  if (attackedPositions.getHeight != height || attackedPositions.getWidth != width)
    throw new RuntimeException("Grid dimensions do not match state dimensions")

  val playerGrid: Grid[Option[Ship]] = createGridFromShips()

  private def createGridFromShips(): Grid[Option[Ship]] = {
    val grid = ArrayGrid.empty[Option[Ship]](None, width, height)

    val setGridPositionToShip = (x: Int, y: Int, ship: Ship) => {
      if (x < 0 || x >= width || y < 0 || y >= height) {
        throw new OutOfBoundsShipException(s"Ship $ship is outside bounds")
      }
      if (grid.get(x, y).isDefined)
        throw new OverlappingShipsException(s"Cannot overlap ships at $x $y")
      grid.set(x, y, Some(ship))
    }

    for (ship <- playerShips) {
      sideEffectForShip(ship, setGridPositionToShip)
    }

    grid
  }

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
    val hasUnattackedPosition = existsForShip(ship, !playerState.attackedPositions.get(_, _))
    !hasUnattackedPosition
  }

  def isAllSunk(): Boolean = {
    for (ship <- playerShips) {
      if (!isShipSunk(ship, this)) {
        return false
      }
    }
    true
  }

  private def existsForShip[A](ship: Ship, test: (Int, Int) => Boolean): Boolean = {
    doForShip(ship, (x: Int, y: Int, _: Ship) => {
      if (test(x, y)) {
        Some(true)
      } else {
        None
      }
    }).getOrElse(false)
  }

  private def sideEffectForShip[A](ship: Ship, sideEffectF: (Int, Int, Ship) => Unit): Unit = {
    doForShip(ship, (x: Int, y: Int, ship: Ship) => {
      sideEffectF(x, y, ship)
      None
    })
  }

  private def doForShip[A](ship: Ship, action: (Int, Int, Ship) => Option[A]): Option[A] = {
    for (l <- 0 until ship.size) {
      val result = if (ship.isHorizontal) {
        action(ship.x + l, ship.y, ship)
      } else {
        action(ship.x, ship.y + l, ship)
      }

      if (result.isDefined) {
        return result
      }
    }
    None
  }
}
