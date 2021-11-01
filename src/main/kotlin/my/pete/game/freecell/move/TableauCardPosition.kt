package my.pete.game.freecell.move

import my.pete.common.Card

class TableauCardPosition(val columnIndex: Int, val numberOfCard: Int, val cardValue: Card) {

    override fun hashCode(): Int {
        return columnIndex + numberOfCard + cardValue.hashCode()
    }
}