package com.gameco.battleship.game

import com.gameco.battleship.entity.Position
import com.gameco.battleship.game.entity._
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

class BattleshipGameSpec extends Specification with Mockito {

  trait GameData extends Scope {
    val opposingPlayerState: PlayerState = mock[PlayerState]
    val attackPosition = Position(5, 5)

    opposingPlayerState.isAttacked(attackPosition.x, attackPosition.y) returns false

    val newOpposingPlayerState = mock[PlayerState]
    opposingPlayerState.setAttacked(attackPosition) returns ((Hit, newOpposingPlayerState))

    val currentPlayerState: PlayerState = mock[PlayerState]

    val battleshipGameState = BattleshipGameState(currentPlayerState, opposingPlayerState)
    val battleshipGame = BattleshipGame(10, 10, battleshipGameState, true, None)
  }

  "Battleship game" should {
    "when taking a turn" in {
      "when there is an error" in {
        "should return out-of-bounds error when the hit position is outside the game width" in {
          //Given
          val width = 10
          val battleshipGame = BattleshipGame(width, 10, mock[BattleshipGameState], true, None)

          //When
          val resultA = battleshipGame.takeTurnForCurrentPlayer(Position(-1, 5))
          val resultB = battleshipGame.takeTurnForCurrentPlayer(Position(10, 5))

          //Then
          resultA must beEqualTo(Left(OutsideBounds))
          resultB must beEqualTo(Left(OutsideBounds))
        }

        "should return out-of-bounds error when the hit position is outside the game height" in {
          //Given
          val height = 10
          val battleshipGame = BattleshipGame(10, height, mock[BattleshipGameState], true, None)

          //When
          val resultA = battleshipGame.takeTurnForCurrentPlayer(Position(5, -1))
          val resultB = battleshipGame.takeTurnForCurrentPlayer(Position(5, 10))

          //Then
          resultA must beEqualTo(Left(OutsideBounds))
          resultB must beEqualTo(Left(OutsideBounds))
        }

        "should return already hit error when the hit position was attacked previously" in {
          //Given
          val playerBState = mock[PlayerState]
          val attackPosition = Position(5, 5)
          playerBState.isAttacked(attackPosition.x, attackPosition.y) returns true
          val battleshipGameState = BattleshipGameState(mock[PlayerState], playerBState)
          val battleshipGame = BattleshipGame(10, 10, battleshipGameState, true, None)

          //When
          val result = battleshipGame.takeTurnForCurrentPlayer(Position(5, 5))

          //Then
          result must beEqualTo(Left(AlreadyHit))
        }

        "should return game over error when the game has already been won by a player" in {
          val battleshipGameState = BattleshipGameState(mock[PlayerState], mock[PlayerState])
          val battleshipGame = BattleshipGame(10, 10, battleshipGameState, true, Some(true))

          //When
          val result = battleshipGame.takeTurnForCurrentPlayer(Position(5, 5))

          //Then
          result must beEqualTo(Left(GameOver))
        }
      }

      "when the move is legal" in {
        "it should be the next player's turn in the new game" in {
          new GameData {
            //When
            val result = battleshipGame.takeTurnForCurrentPlayer(Position(5, 5))

            //Then
            result.isRight must beTrue
            val (_, newGame) = result.right.get
            newGame.isPlayerATurn must beEqualTo(false)
          }
        }

        "then the new game should have the same height, width and current player state as the current game" in {
          new GameData {
            //When
            val result = battleshipGame.takeTurnForCurrentPlayer(Position(5, 5))

            //Then
            result.isRight must beTrue
            val (_, newGame) = result.right.get
            newGame.width must beEqualTo(battleshipGame.width)
            newGame.height must beEqualTo(battleshipGame.height)
            newGame.gameState.playerA must beEqualTo(currentPlayerState)
          }
        }

        "then a Right with the result and new opposing player state should be returned " in {
          new GameData {
            //When
            val result = battleshipGame.takeTurnForCurrentPlayer(Position(5, 5))
            println("Result is: " + result)

            //Then
            result.isRight must beTrue
            val (actionResult, newGame) = result.right.get
            actionResult must beEqualTo(Hit)
            newGame.gameState.playerA must beEqualTo(currentPlayerState)
            newGame.gameState.playerB must beEqualTo(newOpposingPlayerState)
          }
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
