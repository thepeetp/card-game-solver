package my.pete.game.freecell.reader

import my.pete.game.freecell.FreeCellGame

interface FreeCellGameReader {
    fun read(): FreeCellGame
}