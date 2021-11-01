package my.pete.game.freecell.move

import my.pete.game.freecell.FreeCellGame

class FreeCellCoach(var maxScore: Int = 0) {

    private var minMovement: Int = Int.MAX_VALUE

    fun updateMinMovement(doneGame: FreeCellGame) {
        val currentMovement = doneGame.movementHistory.size
        if (currentMovement < minMovement) {
            minMovement = currentMovement
        }

    }

    fun updateScore(game: FreeCellGame) {
        val currentScore = calculateScore(game)
        if (currentScore > maxScore) {
            maxScore = currentScore
        }
    }

    private fun calculateScore(game: FreeCellGame): Int {
        return game.homeCell.getScore()
    }

    fun shouldGiveUp(game: FreeCellGame) = if (minMovement != null) {
        game.movementHistory.size > minMovement
    } else false
}