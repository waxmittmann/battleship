package com.gameco.battleship.game.entity

sealed trait GridStatus
case object UndamagedShip extends GridStatus
case object DamagedShip extends GridStatus
case object AttackedSea extends GridStatus
case object Sea extends GridStatus
