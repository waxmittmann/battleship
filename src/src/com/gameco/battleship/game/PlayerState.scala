package com.gameco.battleship.game

import com.gameco.battleship.entity.{Grid, Position}

case class PlayerState(playerStatus: Grid[GridStatus]) {
  def hitLocation(attackPosition: Position): PlayerState = {
    playerStatus.get(attackPosition.x, attackPosition.y) match {
      case Sea => PlayerState(playerStatus.copyWithChange(attackPosition, AttackedSea))
      case (UndamagedShipEnd | UndamagedShipMidsection) => {
        for (i <- 0 to Integer.MAX_VALUE) { //Todo: Do infinite... though for our sizes this would work =p
          val status: GridStatus = playerStatus.get(attackPosition.x - i, attackPosition.y)
          if (attackPosition.x - i >= 0 && status == UndamagedShipEnd)
        }

        PlayerState(playerStatus.copyWithChange(attackPosition, AttackedSea))
      }
    }
  }
}
