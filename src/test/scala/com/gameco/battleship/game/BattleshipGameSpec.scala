package com.gameco.battleship.game

import com.gameco.battleship.entity.{ArrayGrid, Position}
import com.gameco.battleship.game.entity._
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

class BattleshipGameSpec extends Specification with Mockito {
  val emptyState = PlayerState(10, 10, List(), ArrayGrid.empty(false, 10, 10))

  trait GameData extends Scope {
    val attackPosition = Position(5, 5)

    val opposingPlayerState: PlayerState = mock[PlayerState]
    opposingPlayerState.width returns 10
    opposingPlayerState.height returns 10
    opposingPlayerState.isAttacked(attackPosition.x, attackPosition.y) returns false

    val newOpposingPlayerState = mock[PlayerState]
    newOpposingPlayerState.width returns 10
    newOpposingPlayerState.height returns 10
    opposingPlayerState.setAttacked(attackPosition) returns ((Hit, newOpposingPlayerState))

    val currentPlayerState: PlayerState = mock[PlayerState]
    currentPlayerState.width returns 10
    currentPlayerState.height returns 10

    val battleshipGameState = BattleshipGameState(currentPlayerState, opposingPlayerState)
    val battleshipGame = BattleshipGame(battleshipGameState, true, None)
  }

  "Battleship game" should {

    "when taking a turn" in {

      "when there is an error" in {
        "should return out-of-bounds error when the hit position is outside the game width" in {
          new GameData {
            //When
            val resultA = battleshipGame.takeTurnForCurrentPlayer(Position(-1, 5))
            val resultB = battleshipGame.takeTurnForCurrentPlayer(Position(10, 5))

            //Then
            resultA must beEqualTo(Left(OutsideBounds))
            resultB must beEqualTo(Left(OutsideBounds))
          }
        }

        "should return out-of-bounds error when the hit position is outside the game height" in {
          new GameData {
            //When
            val resultA = battleshipGame.takeTurnForCurrentPlayer(Position(5, -1))
            val resultB = battleshipGame.takeTurnForCurrentPlayer(Position(5, 10))

            //Then
            resultA must beEqualTo(Left(OutsideBounds))
            resultB must beEqualTo(Left(OutsideBounds))
          }
        }

        "should return already hit error when the hit position was attacked previously" in {
          //Given
          val playerBState = mock[PlayerState]
          playerBState.width returns 10
          playerBState.height returns 10
          val attackPosition = Position(5, 5)
          playerBState.isAttacked(attackPosition.x, attackPosition.y) returns true
          val battleshipGameState = BattleshipGameState(emptyState, playerBState)
          val battleshipGame = BattleshipGame(battleshipGameState, true, None)

          //When
          val result = battleshipGame.takeTurnForCurrentPlayer(Position(5, 5))

          //Then
          result must beEqualTo(Left(AlreadyHit))
        }

        "should return game over error when the game has already been won by a player" in {
          val battleshipGameState = BattleshipGameState(emptyState, emptyState)
          val battleshipGame = BattleshipGame(battleshipGameState, true, Some(true))

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

        "then the new game should have the same current player state as the current game" in {
          new GameData {
            //When
            val result = battleshipGame.takeTurnForCurrentPlayer(Position(5, 5))

            //Then
            result.isRight must beTrue
            val (_, newGame) = result.right.get
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
      }
    }

    "when completing a turn and checking for game-over status" in {
      "return an empty option when at the end of a player's turn the opponent still has unsunk ships" in {
        new GameData {
          //Given
          opposingPlayerState.isAllSunk() returns false

          //When
          val result = battleshipGame.takeTurnForCurrentPlayer(Position(5, 5))

          //Then
          result.isRight must beTrue
          val (_, newGame) = result.right.get
          newGame.isPlayerAWinner must beEqualTo(None)
        }
      }

      "return victory for the player when all of the opponent's ships are sunk at the end of the turn" in {
        new GameData {
          //Given
          newOpposingPlayerState.isAllSunk() returns true

          //When
          val result = battleshipGame.takeTurnForCurrentPlayer(Position(5, 5))

          //Then
          result.isRight must beTrue
          val (_, newGame) = result.right.get
          newGame.isPlayerAWinner must beEqualTo(Some(true))
        }
      }
    }
  }

}
