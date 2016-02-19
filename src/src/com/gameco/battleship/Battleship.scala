package com.gameco.battleship

import BattleshipGame.ShipPositionOrientation

object BattleshipGame {
  type TurnResult = (TurnResult, BattleshipGame)
  type ShipPositionOrientation = (Position, Boolean)

  def main(ars: Array[String]): Unit = {
    println("Fooo")
  }
}

case class Position(x: Int, y: Int)

case class Ships(shipPositions: Map[Int, List[ShipPositionOrientation]])

case class BattleshipGameState(playerA: PlayerState, playerB: PlayerState)

case class PlayerState(playerShips: Ships, playerHits: Seq[Position])

sealed trait ActionError
case object OutsideBounds extends ActionError
case object AlreadyHit extends ActionError
case object GameOver extends ActionError

sealed trait TurnResult
case object Win extends TurnResult
case object Hit extends TurnResult
case object Sunk extends TurnResult
case object Miss extends TurnResult

trait Grid[A] {
  def getWidth: Int
  def getHeight: Int
  def get(x: Int, y: Int): A
}

case class ArrayGrid[A](initVal: A, width: Int, height: Int) extends Grid[A] {
  val values: Array[Array[A]] = Array.ofDim(width, height)
  for {
    row <- 0 until height
    col <- 0 until width
  } yield {
    values(col)(row) = initVal
  }

  override def getWidth: Int = width
  override def getHeight: Int = height

  override def get(x: Int, y: Int): A = values(x)(y)

  def set(x: Int, y: Int, value: A): Unit = {
    values(x)(y) = value
  }
}

case class BattleshipGame(width: Int, height: Int, gameState: BattleshipGameState, isPlayerATurn: Boolean) {

  def takeTurn(attackPosition: Position): Either[ActionError, TurnResult] = {
    if (gameState.playerA.playerHits.exists(attackPosition == _)) {
      Left(AlreadyHit)
    } else if (attackPosition.x < 0 || attackPosition.x >= width || attackPosition.y < 0 || attackPosition.y >= height) {
      Left(OutsideBounds)
    } else {
      val hadHit = gameState.playerB.playerShips.shipPositions.exists((ships: (Int, List[ShipPositionOrientation])) => {
          val (size, shipPositionOrientations) = (ships._1, ships._2)
          shipPositionOrientations.exists(shipPositionOrientation => {
            val shipPosition = shipPositionOrientation._1
            val isHorizontal = shipPositionOrientation._2
            if (isHorizontal && shipPosition.y == attackPosition.y
              && (attackPosition.x >= shipPosition.x && attackPosition.x < shipPosition.x + size)) {
              //Is horizontal
              true
            } else if (!isHorizontal && shipPosition.x == attackPosition.x
                && (attackPosition.y >= shipPosition.y && attackPosition.y < shipPosition.y + size)) { //Is horizontal
              true
            } else {
              false
            }
          })
      })

      if (hadHit) {
        Right(Hit)
      } else {
        Right(Miss)
      }
    }
  }


  def takePlayerATurn(attackPosition: (Int, Int)): Either[ActionError, TurnResult] = ???
  def takePlayerBTurn(attackPosition: (Int, Int)): Either[ActionError, TurnResult] = ???

  def getPlayerPosition(): Grid = ???
  def getOpponentPosition(): Grid = ???

  def isPlayerAWinner(): Option[Boolean] = ???
}
