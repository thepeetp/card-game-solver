package my.pete.game.freecell.move

import my.pete.common.Card
import my.pete.game.freecell.FreeCellGame

class TableauToHomeCellMove(private val columnIndex: Int, private val card: Card, private val priority: Int = 0) :
    FreeCellCardMove {
    override fun move(game: FreeCellGame) {
        game.homeCell.deposit(game.tableau[columnIndex].removeLast())
    }

    override fun getPriority(): Int {
        return priority
    }

    override fun toString(): String {
        return "Deposit $card HomeCell"
    }
}