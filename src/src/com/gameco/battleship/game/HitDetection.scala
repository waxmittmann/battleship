package com.gameco.battleship.game

import com.gameco.battleship.entity.{Grid, Position}
import com.gameco.battleship.game.BattleshipGame.ShipPositionOrientation

object HitDetection {
  def isHit(attackPosition: Position, shipsToHit: Grid[GridStatus]): Boolean = {
    if (shipsToHit.get())
  }
}
