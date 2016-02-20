package com.gameco.battleship.game

sealed trait ActionResult
case object Hit extends ActionResult
case object Sunk extends ActionResult
case object Miss extends ActionResult
