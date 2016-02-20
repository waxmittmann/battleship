package com.gameco.battleship.game

import com.gameco.battleship.game.BattleshipGame.ShipPositionOrientation

case class Ships(shipPositions: Map[Int, List[ShipPositionOrientation]])
