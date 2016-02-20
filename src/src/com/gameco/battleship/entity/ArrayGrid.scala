package com.gameco.battleship.entity

import scala.reflect.ClassTag

case class ArrayGrid[A: ClassTag](initVal: A, width: Int, height: Int) extends Grid[A] {
  val values: Array[Array[A]] = Array.ofDim[A](width, height)
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

  override def copyWithChange(position: Position, changeTo: A): Grid[A] = {
    val copiedGrid: ArrayGrid[A] = this.copy()
    copiedGrid.set(position.x, position.y, changeTo)
    copiedGrid
  }
}
