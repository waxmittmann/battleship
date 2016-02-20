package com.gameco.battleship.game

import com.gameco.battleship.entity.Position
import com.gameco.battleship.game.BattleshipGame.ShipPositionOrientation

object HitDetection {
  def isHit(attackPosition: Position, shipsToHit: Ships): Boolean = {
    shipsToHit.shipPositions.exists((ships: (Int, List[ShipPositionOrientation])) => {
      val (size, shipPositionOrientations) = (ships._1, ships._2)
      shipPositionOrientations.exists(shipPositionOrientation => {
        val shipPosition = shipPositionOrientation._1
        val isHorizontal = shipPositionOrientation._2
        if (isHorizontal && shipPosition.y == attackPosition.y
          && (attackPosition.x >= shipPosition.x && attackPosition.x < shipPosition.x + size)) {
          //Is horizontal
          true
        } else if (!isHorizontal && shipPosition.x == attackPosition.x
          && (attackPosition.y >= shipPosition.y && attackPosition.y < shipPosition.y + size)) { //Is horizontal
          true
        } else {
          false
        }
      })
    })
  }
}
