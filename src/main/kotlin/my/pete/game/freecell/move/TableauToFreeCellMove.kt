package my.pete.game.freecell.move

import my.pete.game.freecell.FreeCellGame

class TableauToFreeCellMove(
    private val columnIndex: Int,
    private val freeCellIndex: Int,
    private val destination: DestinationCardMove,
    private val priority: Int
) :
    FreeCellCardMove {
    override fun move(game: FreeCellGame) {
        if (game.freeCells[freeCellIndex] != null || game.tableau[columnIndex].isEmpty()) {
            throw Exception("Tableau to FreeCell Error")
        }
        val card = game.tableau[columnIndex].removeLast()
        game.freeCells[freeCellIndex] = card
    }

    override fun getPriority(): Int {
        return priority
    }

    override fun getMoveCode(): Int {
        return "tf${columnIndex}${freeCellIndex}".hashCode()
    }

    override fun nextForbidMoveCode(): Int {
        return "ft${freeCellIndex}${columnIndex}".hashCode()
    }

    override fun toString(): String {
        return "Move Tableau from column ${columnIndex + 1} to Free Cell column ${freeCellIndex + 1}"
    }

    override fun hashCode(): Int {
        return "tf${destination}".hashCode()
    }
}