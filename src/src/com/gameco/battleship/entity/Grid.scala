package com.gameco.battleship.entity

import com.gameco.battleship.game.{GridStatus, AttackedSea}

trait Grid[A] {
  def copyWithChange(position: Position, changeTo: A): Grid[A] = ???

  def getWidth: Int
  def getHeight: Int
  def get(x: Int, y: Int): A
  def copy(): Grid[A]
}
