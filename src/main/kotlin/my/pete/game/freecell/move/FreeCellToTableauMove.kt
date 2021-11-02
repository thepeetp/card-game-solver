package my.pete.game.freecell.move

import my.pete.game.freecell.FreeCellGame

class FreeCellToTableauMove(
    private val freeCellIndex: Int,
    private val columnIndex: Int,
    private val destination: DestinationCardMove,
    private val priority: Int = 10
) : FreeCellCardMove {
    override fun move(game: FreeCellGame) {
        val cardToMove = game.freeCells[freeCellIndex]
        if (cardToMove != null) {
            game.tableau[columnIndex].add(cardToMove)
            game.freeCells[freeCellIndex] = null
        }
    }

    override fun getPriority(): Int {
        return priority
    }

    override fun getMoveCode(): Int {
        return "ft${freeCellIndex}${columnIndex}".hashCode()
    }

    override fun nextForbidMoveCode(): Int {
        return "tf${columnIndex}${freeCellIndex}".hashCode()
    }

    override fun toString(): String {
        return "Move ${destination.from} FreeCell from column [${freeCellIndex + 1}] to Tableau column [${columnIndex + 1}]"
    }

    override fun hashCode(): Int {
        return "ft${destination}".hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return hashCode() == other.hashCode()
    }
}