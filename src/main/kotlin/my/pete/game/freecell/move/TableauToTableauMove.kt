package my.pete.game.freecell.move

import my.pete.game.freecell.FreeCellGame
import my.pete.game.freecell.move.PossibleMoveFinder.Companion.canStack

class TableauToTableauMove(
    private val from: TableauCardPosition,
    private val toColumnIndex: Int,
    private val destination: DestinationCardMove,
    private val priority: Int
) : FreeCellCardMove {
    override fun move(game: FreeCellGame) {
        val stackCard = game.tableau[from.columnIndex]
        val cardToMove = stackCard.drop(stackCard.size - from.numberOfCard)
        if (!canStack(cardToMove) || !canStack(cardToMove.first(), game.tableau[toColumnIndex].lastOrNull())) {
            throw Exception("Invalid Tableau to Tableau Move")
        }
        game.tableau[from.columnIndex].removeAll(cardToMove)
        game.tableau[toColumnIndex].addAll(cardToMove)
    }

    override fun getPriority(): Int {
        return priority
    }

    override fun getMoveCode(): Int {
        return hashCode()
    }

    override fun nextForbidMoveCode(): Int {
        return 0
    }

    override fun toString(): String {
        return "Move Between Tableau ${from.cardValue} from Column[${from.columnIndex + 1}] to Column[${toColumnIndex + 1}]"
    }

    override fun hashCode(): Int {
        return "tt${destination}".hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return hashCode() == other.hashCode()
    }
}