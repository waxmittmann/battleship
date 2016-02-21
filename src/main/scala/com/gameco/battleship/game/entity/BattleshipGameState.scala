package com.gameco.battleship.game.entity

case class BattleshipGameState(playerA: PlayerState, playerB: PlayerState) {

  def addShipForPlayerA(ship: Ship): Either[Unit, BattleshipGameState] = {
    val writer = (state: PlayerState) => this.copy(playerA = state)
    addShip(ship, playerA, writer)
  }

  def addShipForPlayerB(ship: Ship): Either[Unit, BattleshipGameState] = {
    val writer = (state: PlayerState) => this.copy(playerB = state)
    addShip(ship, playerB, writer)
  }

  //Todo: Check for out of bounds
  private def addShip(ship: Ship, playerState: PlayerState, stateWriter: PlayerState => BattleshipGameState): Either[Unit, BattleshipGameState] = {
    for (i <- 0 until ship.size) {
      if ((ship.isHorizontal && playerState.playerGrid.get(ship.x + i, ship.y).isDefined) ||
        (!ship.isHorizontal && playerState.playerGrid.get(ship.x, ship.y + i).isDefined)) {
        return Left()
      }
    }

    val newPlayerShips = ship :: playerState.playerShips
//    val gridChanges = for (i <- 0 until ship.size) yield {
//      if (ship.isHorizontal) {
//        (ship.x + i, ship.y, Some(ship))
//      } else {
//        (ship.x, ship.y + i, Some(ship))
//      }
//    }

//    val newPlayerGrid = playerState.playerGrid.copyWithChanges(gridChanges)

    Right(stateWriter(playerState.copy(playerShips = newPlayerShips)))
  }
}
