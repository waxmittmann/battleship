package com.gameco.battleship.entity

import scala.collection.immutable.IndexedSeq

trait Grid[A] {

  def getWidth: Int

  def getHeight: Int

  def get(x: Int, y: Int): A

  def copyWithChange(position: Position, changeTo: A): Grid[A]

  def toString(f: A => String): String

  def copyWithChanges(gridChanges: Seq[(Int, Int, A)]): Grid[A]
}