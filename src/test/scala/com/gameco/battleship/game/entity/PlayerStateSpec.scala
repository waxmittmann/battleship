package com.gameco.battleship.game.entity

import com.gameco.battleship.entity.{Position, ArrayGrid, Grid}
import org.specs2.mutable.Specification

class PlayerStateSpec extends Specification {

  //isAttacked, setAttacked, isAllSunk

  "Player state " should {

//    "creation" in {
//      "should fail if the attack grid dimensions do not match the state dimensions" in {
//        false
//      }
//
//      "should fail if a ship is outside the bounds of the player state" in {
//        false
//      }
//    }

    "isAttacked" in {
      false
    }

    "setAttacked" in {
      
      "should hit a horizontal ship at the left end of the ship" in {
        //Given
        val ships: Seq[Ship] = Seq(Ship(1, 1, 3, true))
        val attackedSoFar: ArrayGrid[Boolean] = ArrayGrid.empty[Boolean](false, 10, 10)
        val playerState = PlayerState(10, 10, ships, attackedSoFar)
        val attackPosition: Position = Position(1, 1)

        //When
        val result = playerState.setAttacked(attackPosition)

        //Then
        val expectedHitState = attackedSoFar.copyWithChange(attackPosition, true)
        result._1 must beEqualTo(Hit)
        result._2.attackedPositions must beEqualTo(expectedHitState)
        result._2.playerShips must beEqualTo(ships)
      }

      "should hit a horizontal ship in the middle" in {
        //Given
        val ships: Seq[Ship] = Seq(Ship(1, 1, 3, true))
        val attackedSoFar: ArrayGrid[Boolean] = ArrayGrid.empty[Boolean](false, 10, 10)
        val playerState = PlayerState(10, 10, ships, attackedSoFar)
        val attackPosition: Position = Position(2, 1)

        //When
        val result = playerState.setAttacked(attackPosition)

        //Then
        val expectedHitState = attackedSoFar.copyWithChange(attackPosition, true)
        result._1 must beEqualTo(Hit)
        result._2.attackedPositions must beEqualTo(expectedHitState)
        result._2.playerShips must beEqualTo(ships)
      }

      "should hit a horizontal ship at the right end of the ship" in {
        //Given
        val ships: Seq[Ship] = Seq(Ship(1, 1, 3, true))
        val attackedSoFar: ArrayGrid[Boolean] = ArrayGrid.empty[Boolean](false, 10, 10)
        val playerState = PlayerState(10, 10, ships, attackedSoFar)
        val attackPosition: Position = Position(3, 1)

        //When
        val result = playerState.setAttacked(attackPosition)

        //Then
        val expectedHitState = attackedSoFar.copyWithChange(attackPosition, true)
        result._1 must beEqualTo(Hit)
        result._2.attackedPositions must beEqualTo(expectedHitState)
        result._2.playerShips must beEqualTo(ships)
      }

      "should miss a horizontal ship when hitting left of it" in {
        //Given
        val ships: Seq[Ship] = Seq(Ship(1, 1, 3, true))
        val attackedSoFar: ArrayGrid[Boolean] = ArrayGrid.empty[Boolean](false, 10, 10)
        val playerState = PlayerState(10, 10, ships, attackedSoFar)
        val attackPosition: Position = Position(0, 1)

        //When
        val result = playerState.setAttacked(attackPosition)

        //Then
        val expectedHitState = attackedSoFar.copyWithChange(attackPosition, true)
        result._1 must beEqualTo(Miss)
        result._2.attackedPositions must beEqualTo(expectedHitState)
        result._2.playerShips must beEqualTo(ships)
      }

      "should miss a horizontal ship when hitting right of it" in {
        //Given
        val ships: Seq[Ship] = Seq(Ship(1, 1, 3, true))
        val attackedSoFar: ArrayGrid[Boolean] = ArrayGrid.empty[Boolean](false, 10, 10)
        val playerState = PlayerState(10, 10, ships, attackedSoFar)
        val attackPosition: Position = Position(4, 1)

        //When
        val result = playerState.setAttacked(attackPosition)

        //Then
        val expectedHitState = attackedSoFar.copyWithChange(attackPosition, true)
        result._1 must beEqualTo(Miss)
        result._2.attackedPositions must beEqualTo(expectedHitState)
        result._2.playerShips must beEqualTo(ships)
      }

      "should hit a vertical ship at the bottom end of the ship" in {
        //Given
        val ships: Seq[Ship] = Seq(Ship(3, 4, 3, false))
        val attackedSoFar: ArrayGrid[Boolean] = ArrayGrid.empty[Boolean](false, 10, 10)
        val playerState = PlayerState(10, 10, ships, attackedSoFar)
        val attackPosition: Position = Position(3, 4)

        //When
        val result = playerState.setAttacked(attackPosition)

        //Then
        val expectedHitState = attackedSoFar.copyWithChange(attackPosition, true)
        result._1 must beEqualTo(Hit)
        result._2.attackedPositions must beEqualTo(expectedHitState)
        result._2.playerShips must beEqualTo(ships)
      }

      "should hit a vertical ship in the middle" in {
        //Given
        val ships: Seq[Ship] = Seq(Ship(3, 4, 3, false))
        val attackedSoFar: ArrayGrid[Boolean] = ArrayGrid.empty[Boolean](false, 10, 10)
        val playerState = PlayerState(10, 10, ships, attackedSoFar)
        val attackPosition: Position = Position(3, 5)

        //When
        val result = playerState.setAttacked(attackPosition)

        //Then
        val expectedHitState = attackedSoFar.copyWithChange(attackPosition, true)
        result._1 must beEqualTo(Hit)
        result._2.attackedPositions must beEqualTo(expectedHitState)
        result._2.playerShips must beEqualTo(ships)
      }

      "should hit a vertical ship at the top end of the ship" in {
        //Given
        val ships: Seq[Ship] = Seq(Ship(3, 4, 3, false))
        val attackedSoFar: ArrayGrid[Boolean] = ArrayGrid.empty[Boolean](false, 10, 10)
        val playerState = PlayerState(10, 10, ships, attackedSoFar)
        val attackPosition: Position = Position(3, 6)

        //When
        val result = playerState.setAttacked(attackPosition)

        //Then
        val expectedHitState = attackedSoFar.copyWithChange(attackPosition, true)
        result._1 must beEqualTo(Hit)
        result._2.attackedPositions must beEqualTo(expectedHitState)
        result._2.playerShips must beEqualTo(ships)
      }

      "should miss a vertical ship at the when shooting below it" in {
        //Given
        val ships: Seq[Ship] = Seq(Ship(3, 4, 3, false))
        val attackedSoFar: ArrayGrid[Boolean] = ArrayGrid.empty[Boolean](false, 10, 10)
        val playerState = PlayerState(10, 10, ships, attackedSoFar)
        val attackPosition: Position = Position(3, 3)

        //When
        val result = playerState.setAttacked(attackPosition)

        //Then
        val expectedHitState = attackedSoFar.copyWithChange(attackPosition, true)
        result._1 must beEqualTo(Miss)
        result._2.attackedPositions must beEqualTo(expectedHitState)
        result._2.playerShips must beEqualTo(ships)
      }

      "should miss a vertical ship at the when shooting above it" in {
        //Given
        val ships: Seq[Ship] = Seq(Ship(3, 4, 3, false))
        val attackedSoFar: ArrayGrid[Boolean] = ArrayGrid.empty[Boolean](false, 10, 10)
        val playerState = PlayerState(10, 10, ships, attackedSoFar)
        val attackPosition: Position = Position(3, 7)

        //When
        val result = playerState.setAttacked(attackPosition)

        //Then
        val expectedHitState = attackedSoFar.copyWithChange(attackPosition, true)
        result._1 must beEqualTo(Miss)
        result._2.attackedPositions must beEqualTo(expectedHitState)
        result._2.playerShips must beEqualTo(ships)
      }

      "should return 'Hit' and a new state with the attack position set to true when there is a ship that is not yet" +
        "completely sunk at that position" in {
        //Given
        val ships: Seq[Ship] = Seq(Ship(5, 5, 3, true))
        val attackedSoFar: ArrayGrid[Boolean] = ArrayGrid.empty[Boolean](false, 10, 10)
        val playerState = PlayerState(10, 10, ships, attackedSoFar)
        val attackPosition: Position = Position(5, 5)

        //When
        val result = playerState.setAttacked(attackPosition)

        //Then
        val expectedHitState = attackedSoFar.copyWithChange(attackPosition, true)
        result._1 must beEqualTo(Hit)
        result._2.attackedPositions must beEqualTo(expectedHitState)
        result._2.playerShips must beEqualTo(ships)
      }

      "should return 'Sunk' and a new state with the attack position set to true when there is a ship that is completely " +
        "sunk (all its segments are marked as hit)" in {
        //Given
        val ships: Seq[Ship] = Seq(Ship(5, 5, 3, true))
        val attackedSoFar: ArrayGrid[Boolean] = ArrayGrid.empty(false, 10, 10)
        attackedSoFar.set(6, 5, true)
        attackedSoFar.set(7, 5, true)
        val playerState = PlayerState(10, 10, ships, attackedSoFar)
        val attackPosition: Position = Position(5, 5)

        //When
        val result = playerState.setAttacked(attackPosition)

        //Then
        val expectedHitState = attackedSoFar.copyWithChange(attackPosition, true)
        result._1 must beEqualTo(Sunk)
        result._2.attackedPositions must beEqualTo(expectedHitState)
        result._2.playerShips must beEqualTo(ships)
      }

      "should return 'Miss' and a new state with the attack position set to true when there is no ship at the location" in {
        //Given
        val ships: Seq[Ship] = Seq(Ship(0, 0, 3, true))
        val attackedSoFar: ArrayGrid[Boolean] = ArrayGrid.empty(false, 10, 10)
        val playerState = PlayerState(10, 10, ships, attackedSoFar)
        val attackPosition: Position = Position(5, 5)

        //When
        val result = playerState.setAttacked(attackPosition)

        //Then
        val expectedHitState = attackedSoFar.copyWithChange(attackPosition, true)
        result._1 must beEqualTo(Miss)
        result._2.attackedPositions must beEqualTo(expectedHitState)
        result._2.playerShips must beEqualTo(ships)
      }
    }
  }
}
