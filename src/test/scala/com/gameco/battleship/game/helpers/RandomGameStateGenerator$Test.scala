package com.gameco.battleship.game.helpers

import org.specs2.mutable.Specification

class RandomGameStateGenerator$Test extends Specification {

  //In prod, would probably inject random seed for consistency
  "Generator" should {
    "generate a game state where both sides have the correct number of ships" in {
      //When
      val gameState = RandomGameStateGenerator.create(10, 10)

      //Then
      var totalSumA = 0
      var totalSumB = 0

      for {
        col <- 0 until gameState.playerA.height
        row <- 0 until gameState.playerA.width
      } {
        if (gameState.playerA.playerGrid.get(row, col).isDefined) {
          totalSumA = totalSumA + 1
        }
        if (gameState.playerB.playerGrid.get(row, col).isDefined) {
          totalSumB = totalSumB + 1
        }
      }

      totalSumA must beEqualTo(15)
      totalSumB must beEqualTo(15)
    }
  }

}
