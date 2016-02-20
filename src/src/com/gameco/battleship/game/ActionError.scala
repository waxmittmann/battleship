package com.gameco.battleship.game

sealed trait ActionError
case object OutsideBounds extends ActionError
case object AlreadyHit extends ActionError
case object GameOver extends ActionError
