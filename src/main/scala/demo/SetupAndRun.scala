package demo

import com.gameco.battleship.entity.Position
import com.gameco.battleship.game.BattleshipGame
import com.gameco.battleship.game.entity.{ActionResult, ActionError, Ship, BattleshipGameState}

object SetupAndRun {
  def main(args: Array[String]): Unit = {
    val playerAShips = List(
      Ship(1, 2, 3, true),
      Ship(1, 2, 3, true),
      Ship(1, 2, 3, true))

    val playerBShips = List(
      Ship(1, 2, 3, true),
      Ship(1, 2, 3, true),
      Ship(1, 2, 3, true))

    val initialState = BattleshipGameState.create(10, 10, playerAShips, playerBShips)

    val game = BattleshipGame.create(10, 10, initialState)

    val moves =
      List(Position(0, 0),
        Position(0, 1),
        Position(2, 0),
        Position(1, 1),
        Position(3, 1),
        Position(4, 2),
        Position(0, 2))

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

    println(moveResults._2)
  }
}
