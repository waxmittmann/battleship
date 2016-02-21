package com.gameco.battleship.end2end

import com.gameco.battleship.entity.Position
import com.gameco.battleship.game.BattleshipGame
import com.gameco.battleship.game.entity._
import org.specs2.mutable.Specification

class BattleshipEndToEndSpec extends Specification {

  "The game" should {
    "run correctly " in {
      val playerAShips = List(
        Ship(1, 0, 2, false),
        Ship(0, 3, 3, true),
        Ship(4, 4, 1, true))

      val playerBShips = List(
        Ship(2, 0, 4, false),
        Ship(4, 0, 1, true))

      val initialState = BattleshipGameState.create(5, 5, playerAShips, playerBShips)

      val game = BattleshipGame.create(5, 5, initialState)

      val moves =
        List(
          Position(1, 0), //PlayerA
          Position(2, 0), //PlayerB
          Position(2, 0), //PlayerA
          Position(2, 1), //PlayerB
          Position(4, 4), //PlayerA
          Position(2, 1), //Hit twice
          Position(5, 2), //Outside bounds
          Position(2, 3), //PlayerB
          Position(2, 1), //PlayerA
          Position(2, 2), //PlayerB
          Position(2, 2), //PlayerA
          Position(0, 3), //PlayerB
          Position(2, 3), //PlayerA
          Position(4, 4), //PlayerB
          Position(4, 0), //PlayerA
          Position(3, 3)) //PlayerB

      val expected = List(
        Right(Miss),
        Right(Miss),
        Right(Hit),
        Right(Miss),
        Right(Miss),
        Left(AlreadyHit),
        Left(OutsideBounds),
        Right(Hit),
        Right(Hit),
        Right(Miss),
        Right(Hit),
        Right(Hit),
        Right(Sunk),
        Right(Sunk),
        Right(Sunk),
        Left(GameOver)
      )

      val moveResults = moves.foldLeft((game, List[Either[ActionError, ActionResult]]()))(
        (memo: (BattleshipGame, List[Either[ActionError, ActionResult]]), cur: Position) => {
          val curGame = memo._1
          val resultsSoFar = memo._2
          val result = curGame.takeTurnForCurrentPlayer(cur).fold(error => {
            (curGame, Left(error) :: resultsSoFar)
          }, result => {
            (result._2, Right(result._1) :: resultsSoFar)
          })
          result
        })

      moveResults._1.isPlayerAWinner must beEqualTo(Some(true))
      moveResults._2.reverse must beEqualTo(expected)
    }
  }
}
