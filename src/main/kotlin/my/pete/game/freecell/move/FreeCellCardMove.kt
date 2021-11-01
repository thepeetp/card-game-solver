package my.pete.game.freecell.move

import my.pete.game.freecell.FreeCellGame


interface FreeCellCardMove {
    fun move(game: FreeCellGame)
    fun getPriority(): Int
    fun getMoveCode(): Int
    fun nextForbidMoveCode(): Int
}