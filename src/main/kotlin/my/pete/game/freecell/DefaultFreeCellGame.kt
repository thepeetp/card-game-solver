package my.pete.game.freecell

import my.pete.common.CardDeck

class DefaultFreeCellGame : FreeCellGame(mutableListOf()) {


    private val tableauSize: Int = 8

    init {
        for (i in 0 until tableauSize) {
            tableau.add(mutableListOf())
        }
        val cardDeck = CardDeck()
        var index = 0
        while (!cardDeck.isEmpty()) {
            val column = tableau[index++]
            column.add(cardDeck.pull())
            if (index >= tableauSize) {
                index = 0
            }
        }
    }
}