package com.gameco.battleship.game.entity

import org.specs2.mutable.Specification

class PlayerStateSpec extends Specification {

  //isAttacked, setAttacked, isAllSunk

  "Player state " should {

    "isAttacked" in {

      "should return 'Hit' and a new state with the attack position set to true when there is a ship that is not yet" +
        "completely sunk" in {
        val playerState = PlayerState()
      }

      "should return 'Sunk' and a new state with the attack position set to true when there is a ship that is completely" +
        "sunk (all its segments are marked as hit)" in {

      }

      "should return 'Miss' and a new state with the attack position set to true when there is no ship at the location" in {

      }

    }

  }

}
