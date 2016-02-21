package com.gameco.battleship.entity

import scala.reflect.ClassTag

object ArrayGrid {
  def empty[A: ClassTag](initalVal: A, width: Int, height: Int): ArrayGrid[A] = {
    val values: Array[Array[A]] = Array.ofDim[A](height, width)
    for {
      row <- 0 until height
      col <- 0 until width
    } yield {
      values(col)(row) = initalVal
    }
    ArrayGrid[A](values, width, height)
  }
}

case class ArrayGrid[A: ClassTag](values: Array[Array[A]], width: Int, height: Int) extends Grid[A] {

  override def getWidth: Int = width
  override def getHeight: Int = height

  override def get(x: Int, y: Int): A = values(y)(x)

  def set(x: Int, y: Int, value: A): Unit = {
    values(y)(x) = value
  }

  override def copyWithChange(position: Position, changeTo: A): Grid[A] = {
    val newGrid: ArrayGrid[A] = deepCopy
    newGrid.set(position.x, position.y, changeTo)
    newGrid
  }

  def deepCopy: ArrayGrid[A] = {
    val newArray: Array[Array[A]] = Array.ofDim[A](height, width)
    for (r <- 0 until height) {
      val newRow: Array[A] = Array.ofDim[A](width)
      Array.copy(this.values(r), 0, newRow, 0, width)
      newArray(r) = newRow
    }

    val newGrid: ArrayGrid[A] = ArrayGrid(newArray, width, height)
    newGrid
  }

  override def toString(): String = {
    values.map(_.mkString(", ")).mkString("\n")
  }

  def toString(f: A => String): String = {
    values.map(_.map(f).mkString(", ")).mkString("\n")
  }

  override def equals(in: Any): Boolean = {
    def compareGrids(otherGrid: ArrayGrid[A]): Boolean = {
      if (width != otherGrid.width || height != otherGrid.height) {
        return false
      }

      for {
        row <- 0 until height
        col <- 0 until width
      } yield {
        if (values(col)(row) != otherGrid.values(col)(row))
          return false
      }

      true
    }

    if (in.isInstanceOf[ArrayGrid[A]]) {
      val otherGrid = in.asInstanceOf[ArrayGrid[A]]
      compareGrids(otherGrid)
    } else {
      false
    }
  }

  override def copyWithChanges(gridChanges: Seq[(Int, Int, A)]): Grid[A] = {
    val result = deepCopy
    gridChanges.foreach(change => result.set(change._1, change._2, change._3))
    result
  }
}
