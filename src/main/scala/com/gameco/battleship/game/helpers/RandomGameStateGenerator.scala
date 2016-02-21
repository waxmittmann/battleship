package com.gameco.battleship.game.helpers

import com.gameco.battleship.entity.ArrayGrid
import com.gameco.battleship.game.entity._

import scala.util.Random

object RandomGameStateGenerator {
  val random = new Random()

  def create(width: Int, height: Int): BattleshipGameState = {
    BattleshipGameState(createState(width, height), createState(width, height))
  }

  private def createState(width: Int, height: Int): PlayerState = {

    val emptyHits = ArrayGrid.empty[Boolean](false, width, height)
    var result = PlayerState(width, height, List(), emptyHits)

    for (size <- 1 to 5) {
      var successfullyInserted = false
      while (!successfullyInserted) {
        val xPos = random.nextInt(width)
        val yPos = random.nextInt(height)
        val isHorizontal = random.nextBoolean()

        try {
          result = PlayerState(width, height, Ship(xPos, yPos, size, isHorizontal) :: result.playerShips, emptyHits)
          successfullyInserted = true
        } catch {
          case (OutOfBoundsShipException(_) | OverlappingShipsException(_)) => ()
        }
      }
    }

    result
  }

}
