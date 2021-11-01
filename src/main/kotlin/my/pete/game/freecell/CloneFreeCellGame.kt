package my.pete.game.freecell

import my.pete.common.Card
import my.pete.game.freecell.move.FreeCellCoach

class CloneFreeCellGame(
    source: FreeCellGame,
    tableau: MutableList<MutableList<Card>> = mutableListOf(),
    freeCells: Array<Card?> = arrayOfNulls(4),
    homeCell: HomeCell = HomeCell(),
    coach: FreeCellCoach
) : FreeCellGame(tableau, freeCells, homeCell, coach = coach) {
    init {
        source.tableau.forEach { sourceTableau ->
            val newTableau = mutableListOf<Card>()
            newTableau.addAll(sourceTableau)
            tableau.add(newTableau)
        }

        source.freeCells.forEachIndexed { index, card ->
            freeCells[index] = card
        }

        source.homeCell.stack.forEach { (key, value) -> homeCell.stack[key] = value }

        movementHistory.addAll(source.movementHistory)
    }
}