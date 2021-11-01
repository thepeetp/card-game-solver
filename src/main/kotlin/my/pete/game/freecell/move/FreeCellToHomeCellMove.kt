package my.pete.game.freecell.move

import my.pete.common.Card
import my.pete.game.freecell.FreeCellGame

class FreeCellToHomeCellMove(private val index: Int, private val card: Card, private val priority: Int = 0) :
    FreeCellCardMove {
    override fun move(game: FreeCellGame) {
        val card = game.freeCells[index]
        if (card != null) {
            game.homeCell.deposit(card)
            game.freeCells[index] = null
        }
    }

    override fun getPriority(): Int {
        return priority
    }

    override fun toString(): String {
        return "Deposit $card to HomeCell"
    }
}