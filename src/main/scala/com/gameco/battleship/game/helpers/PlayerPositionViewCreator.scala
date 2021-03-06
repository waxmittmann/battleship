package com.gameco.battleship.game.helpers

import com.gameco.battleship.entity.{ArrayGrid, Grid}
import com.gameco.battleship.game.entity._

object PlayerPositionViewCreator {
  def getPosition(width: Int, height: Int, playerState: PlayerState, hideUndamagedShips: Boolean): Grid[GridStatus] = {
    val playerPosition = ArrayGrid.empty[GridStatus](Sea, width, height)

    for {
      y <- 0 until height
      x <- 0 until width
    } yield {
      if (playerState.attackedPositions.get(x, y)) {
        if (playerState.playerGrid.get(x, y).isDefined) {
          playerPosition.set(x, y, DamagedShip)
        } else {
          playerPosition.set(x, y, AttackedSea)
        }
      } else {
        if (!hideUndamagedShips && playerState.playerGrid.get(x, y).isDefined) {
          playerPosition.set(x, y, UndamagedShip)
        } else {
          playerPosition.set(x, y, Sea)
        }
      }
    }

    playerPosition
  }
}
