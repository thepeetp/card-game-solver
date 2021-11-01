package my.pete.game.freecell.move

import my.pete.common.Card

class DestinationCardMove(val from: Card, val to: Card?) {
    override fun toString(): String {
        return "$from$to"
    }
}