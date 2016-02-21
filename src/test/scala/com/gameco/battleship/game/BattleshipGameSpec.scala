package com.gameco.battleship.game

import org.specs2.mutable.Specification

class BattleshipGameSpec extends Specification {

  "Battleship game" should {
    "when taking a turn" in {
      "when there is an error" in {
        "should return out-of-bounds error when the hit position is outside the game dimensions" in {
          false
        }

        "should return already hit error when the hit position was attacked previously" in {
          false
        }

        "should return game over error when the game has already been won by a player" in {
          false
        }
      }

      "when the move is legal" in {
        "for playerA and a ship is hit, it should return 'Hit' and a game with updated state showing the hit on playerB" in {
          false
        }

        "for playerB and a ship is hit, it should return 'Hit' and a game with updated state showing the hit on playerA" in {
          false
        }

        "and a ship is sunk, it should return 'Sunk' and a game with updated state showing the hit" in {
          false
        }

        "and it is a miss, it should return 'Miss' and a game with updated state showing the miss" in {
          false
        }
      }
    }

    "when checking for game-over status" in {
      "return an empty option when both players have ships remaining" in {
        false
      }

      "return victory for PlayerA when all of PlayerB's ships have been sunk" in {
        false
      }

      "return victory for PlayerB when all of PlayerA's ships have been sunk" in {
        false
      }
    }
  }

}
