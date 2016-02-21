package com.gameco.battleship.entity

import org.specs2.mutable.Specification

class ArrayGridSpec extends Specification {

  "ArrayGrid copyWithChange" should {
    "leave the initial grid unchanged" in {
      //Given
      val initialGrid = ArrayGrid.empty(false, 5, 5)
      initialGrid.set(2, 1, true)
      initialGrid.set(1, 2, true)

      //When
      initialGrid.copyWithChange(Position(3, 4), true)

      //Then
      initialGrid.get(1, 2) must beEqualTo(true)
      initialGrid.get(2, 1) must beEqualTo(true)
      initialGrid.get(3, 4) must beEqualTo(false)
    }

    "apply the change to the new grid" in {
      //Given
      val initialGrid = ArrayGrid.empty(false, 5, 5)
      initialGrid.set(2, 1, true)
      initialGrid.set(1, 2, true)

      //When
      val newGrid = initialGrid.copyWithChange(Position(3, 4), true)

      //Then
      newGrid.get(1, 2) must beEqualTo(true)
      newGrid.get(2, 1) must beEqualTo(true)
      newGrid.get(3, 4) must beEqualTo(true)
    }
  }

}
