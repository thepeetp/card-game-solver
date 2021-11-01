package my.pete.game.freecell

import my.pete.common.Card
import my.pete.game.freecell.move.FreeCellCardMove
import my.pete.game.freecell.move.FreeCellCoach
import my.pete.game.freecell.move.PossibleMoveFinder

open class FreeCellGame(
    val tableau: MutableList<MutableList<Card>>,
    val freeCells: Array<Card?> = arrayOfNulls(4),
    val homeCell: HomeCell = HomeCell(),
    val movementHistory: MutableList<FreeCellCardMove> = mutableListOf(),
    private val coach: FreeCellCoach = FreeCellCoach()
) {

    private val moveFinder = PossibleMoveFinder(this)


    override fun toString(): String {
        return tableau.last().toString()
    }


    fun findBestWinGame(): FreeCellGame {
        return findWinGame().minByOrNull { it.movementHistory.size }!!
    }

    private fun findWinGame(): List<FreeCellGame> {
        return findPossibleGame().filter { it.isWin() }
    }

    private fun findPossibleGame(): List<FreeCellGame> {
        val possibleDone = mutableListOf<FreeCellGame>()
        findDoneGame(possibleDone)
        return possibleDone
    }


    private fun findDoneGame(possibleDone: MutableList<FreeCellGame>) {
        if (isWin()) {
            coach.updateMinMovement(this)
            possibleDone.add(this)
        }
        if (possibleDone.isNotEmpty()) {
            return
        }
        moveFinder.findPossibleMove().mapNotNull { tryToMove(it) }.forEach { it.findDoneGame(possibleDone) }
    }

    private fun tryToMove(movement: FreeCellCardMove): FreeCellGame? {
        if (shouldStopTry(movement)) {
            return null
        } else if (isWin()) {
            return this
        } else {
            val cloneGame = clone()
            movement.move(cloneGame)
            coach.updateScore(cloneGame)
            cloneGame.movementHistory.add(movement)
            return cloneGame
        }
    }

    private fun shouldStopTry(movement: FreeCellCardMove): Boolean {
        val duplicate = movementHistory.contains(movement)
        return coach.shouldGiveUp(this) || duplicate
    }

    private fun isWin(): Boolean {
        return homeCell.isFull()
    }

    private fun clone(): FreeCellGame {
        return CloneFreeCellGame(this, coach = coach)
    }

    override fun hashCode(): Int {
        return tableau.hashCode() + freeCells.hashCode() + homeCell.hashCode()
    }
}