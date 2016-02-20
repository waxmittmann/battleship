package com.gameco.battleship.entity

/**
 * Created by maxwittman on 20/02/2016.
 */
trait Grid[A] {
  def getWidth: Int
  def getHeight: Int
  def get(x: Int, y: Int): A
}
