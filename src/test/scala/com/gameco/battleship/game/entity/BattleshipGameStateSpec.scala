package com.gameco.battleship.game.entity

import com.gameco.battleship.entity.{ArrayGrid, Grid}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

class BattleshipGameStateSpec extends Specification with Mockito {

  trait TestData extends Scope {
    val playerAShips = List(Ship(x = 2, y = 3, size = 3, isHorizontal = true))
    val playerStateA = PlayerState(10, 10, playerAShips, ArrayGrid.empty[Boolean](false, 10, 10))

    val playerBShips = List(Ship(x = 2, y = 1, size = 6, isHorizontal = true))
    val playerStateB = PlayerState(10, 10, playerAShips, ArrayGrid.empty[Boolean](false, 10, 10))

    val initialBattleshipGameState = BattleshipGameState(playerStateA, playerStateB)
  }

  "BattleshipGameState" should {
    "add ship for PlayerA if it does not overlap another ship" in {
      new TestData {
        //Given
        val shipToAdd = Ship(x = 3, y = 4, size = 2, isHorizontal = false)

        //When
        val result = initialBattleshipGameState.addShipForPlayerA(shipToAdd)

        //Then
        result.isRight must beTrue
        val (newPlayerStateA, newPlayerStateB) = (result.right.get.playerA, result.right.get.playerB)
        newPlayerStateB must beEqualTo(playerStateB)
        newPlayerStateA.playerShips must beEqualTo(shipToAdd :: playerStateA.playerShips)
      }
    }

    "add ship for PlayerB if it does not overlap another ship" in {
      new TestData {
        //Given
        val shipToAdd = Ship(x = 3, y = 2, size = 3, isHorizontal = true)

        //When
        val result = initialBattleshipGameState.addShipForPlayerB(shipToAdd)

        //Then
        result.isRight must beTrue
        val (newPlayerStateA, newPlayerStateB) = (result.right.get.playerA, result.right.get.playerB)
        newPlayerStateA must beEqualTo(playerStateA)
        newPlayerStateB.playerShips must beEqualTo(shipToAdd :: playerStateB.playerShips)
      }
    }

    "fail for PlayerA if the ship overlaps another ship" in {
      new TestData {
        //Given
        val shipToAdd = Ship(x = 3, y = 3, size = 2, isHorizontal = false)

        //When
        val result = initialBattleshipGameState.addShipForPlayerA(shipToAdd)

        //Then
        result.isLeft must beTrue
      }
    }

    "fail for PlayerB if the ship overlaps another ship" in {
      new TestData {
        //Given
        val shipToAdd = Ship(x = 2, y = 3, size = 3, isHorizontal = true)

        //When
        val result = initialBattleshipGameState.addShipForPlayerB(shipToAdd)

        //Then
        result.isLeft must beTrue
      }
    }
  }
}
