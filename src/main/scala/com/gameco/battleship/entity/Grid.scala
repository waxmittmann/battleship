package com.gameco.battleship.entity

trait Grid[A] {
  def getWidth: Int

  def getHeight: Int

  def get(x: Int, y: Int): A

  def copyWithChange(position: Position, changeTo: A): Grid[A]

  def toString(f: A => String): String
}