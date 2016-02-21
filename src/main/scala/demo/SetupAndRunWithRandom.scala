package demo

import com.gameco.battleship.entity.Position
import com.gameco.battleship.game.BattleshipGame
import com.gameco.battleship.game.entity.{ActionError, ActionResult, BattleshipGameState, Ship}
import com.gameco.battleship.game.helpers.RandomGameStateGenerator

object SetupAndRunWithRandom {
  def main(args: Array[String]): Unit = {
    playGame(RandomGameStateGenerator.create(10, 10))
  }

  def playGame(initialState: BattleshipGameState): Unit = {
    val game = BattleshipGame.create(5, 5, initialState)

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
