How to run
-----------
The tests (especially BattleshipEndToEndSpec) demonstrate how to use the Battleship API and are run the usual 'sbt test'
way.

Design
-----------
The implementation has been designed to use immutable state in every method exposed to the user. For example,
BattleshipGameState is transitioned to a new state by creating a mutated copy which is then wrapped by the 'BattleshipGame'
and returned as a new game after each turn.

Test coverage is getting there, but not as thorough as I would like. I would also have liked to implement a game runner
to encapsulate the transitioning from state to state (currently done via a 'foldLeft' in the end-to-end test and demos).

Structure
-----------
> BattleshipGame
 The 'BattleshipGame' class allows players to take turns and allows the player to query the game state, which consists of
 the player's grid and the opponent's grid (undamaged ships are only revealed on the player's grid), as well as of
 a boolean indicating whose turn it is an a boolean option indicating who if anyone has won the game.

 'BattleshipGame' manages transitions from turn to turn, enforces game rules (such as disallowing hitting the same
 field twice) and the game lifecycle from start to game-over.

 The methods:
 - takeTurnForCurrentPlayer(attackPosition) returns Either[ActionError, (ActionResult, BattleshipGame)]: Take turn for
    the current player; returns a Left with an error if the move is illegal (out-of-bounds, already-hit, or game-over)
    or the result of the move and the new game state if the move was legal.

 - getPlayerPosition() / getOpponentPosition() returns a grid of GridStatus(es) where 'GridStatus' is one of 'UndamagedShip' /
     'DamagedShip' / 'AttackedSea' / 'Sea'. The opponent's position will hide 'UndamagedShip' statuses (mark them as 'Sea')

> BattleshipGameState
 This encapsulates the two game states for playerA and playerB. It also allows the adding or removing of ships (which
 is done by returning a copy of the game state with the ship added / removed).

> PlayerState
 The PlayerState stores the position of all of a player's ships, as well as all of the attacks made by the opponent.
 This allows the PlayerState to detect which ships have been hit/sunk, and whether all ships have been hit/sunk.

 The methods:
 - isAttacked(x, y) returns Boolean: check if a grid field has already been attacked
 - setAttacked(x, y) returns ActionResult: attack a grid field; returns whether the result was a 'Hit', 'Miss' or 'Sink'
    ('Sink' means a hit on the last undamaged segment of a ship)
 - isAllSunk(x, y) returns Boolean: True if all ships have been sunk (and hence the opponent has won) or false if undamaged
    ship segments remain

