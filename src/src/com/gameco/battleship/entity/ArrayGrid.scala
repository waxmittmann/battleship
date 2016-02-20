package com.gameco.battleship.entity

/**
 * Created by maxwittman on 20/02/2016.
 */
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
