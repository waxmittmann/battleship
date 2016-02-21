package com.gameco.battleship.game.entity

import com.gameco.battleship.game.BattleshipGame.ShipPositionOrientation

case class Ships(shipPositions: Map[Int, List[ShipPositionOrientation]])
