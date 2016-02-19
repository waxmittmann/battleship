package com.gameco.battleship

object BattleshipGame {
  type TurnResult = (TurnResult, BattleshipGame)
}

case class Ships(shipPositions: List[List[(Int, Int)]])

case class BattleshipGameState(playerA: PlayerState, playerB: PlayerState)

case class PlayerState(playerShips: Ships, playerHits: (Int, Int))

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

trait BattleshipGame {
  protected val gameState: BattleshipGameState

  def takeTurn(attackPosition: (Int, Int)): Either[ActionError, TurnResult]
  def takePlayerATurn(attackPosition: (Int, Int)): Either[ActionError, TurnResult]
  def takePlayerBTurn(attackPosition: (Int, Int)): Either[ActionError, TurnResult]

  def isPlayerATurn(): Boolean
  def getPlayerPosition(): Grid
  def getOpponentPosition(): Grid

  def isPlayerAWinner(): Option[Boolean]
}
