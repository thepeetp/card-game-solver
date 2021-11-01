package my.pete.game.freecell.move

import my.pete.common.Card
import my.pete.common.CardRank
import my.pete.game.freecell.FreeCellGame

class PossibleMoveFinder(private val game: FreeCellGame) {

    fun findPossibleMove(): List<FreeCellCardMove> {
        val moveSet = mutableListOf<FreeCellCardMove>()
        moveSet.addAll(findTableauToHomeCellMove())
        moveSet.addAll(findFreeCellToHomeCellMove())
        if (moveSet.isNotEmpty()) {
            return moveSet
        }
        moveSet.addAll(findTableauToFreeCellMove())
        moveSet.addAll(findFreeCellToTableauMove())
        moveSet.addAll(findTableauToTableauMove())
        return moveSet.sortedBy { it.getPriority() }
    }

    private fun findTableauToHomeCellMove(): List<FreeCellCardMove> {
        val moveSet = mutableListOf<FreeCellCardMove>()
        for ((index, column) in game.tableau.withIndex()) {
            if (column.isEmpty()) {
                continue
            }
            val frontCard = column.last()
            if (game.homeCell.available(frontCard)) {
                moveSet.add(TableauToHomeCellMove(index, frontCard))
            }
        }
        return moveSet
    }

    private fun findFreeCellToHomeCellMove(): List<FreeCellCardMove> {
        val moveSet = mutableListOf<FreeCellCardMove>()
        game.freeCells.forEachIndexed { index, card ->
            if (card != null && game.homeCell.available(card)) {
                moveSet.add(FreeCellToHomeCellMove(index, card))
            }
        }
        return moveSet
    }

    private fun findTableauToTableauMove(): List<FreeCellCardMove> {
        val maxStackMove = maxStackMove()
        val movementList = mutableSetOf<FreeCellCardMove>()
        for (i in 1..maxStackMove) {
            movementList.addAll(findTableauToTableauMove(maxStackMove - i + 1))
        }
        return movementList.toList()
    }

    private fun findTableauToTableauMove(stackMove: Int): List<FreeCellCardMove> {
        val correctStack = findCorrectStack(stackMove)
        return findPossibleCanGo(correctStack)
    }

    private fun findTableauToFreeCellMove(): List<FreeCellCardMove> {
        if (game.freeCells.any { it == null }) {
            val moveSet = mutableListOf<FreeCellCardMove>()
            game.tableau
                .forEachIndexed { columnIndex, cards ->
                    if (cards.isNotEmpty()) {
                        moveSet.add(
                            TableauToFreeCellMove(
                                columnIndex, game.freeCells.indexOfFirst { it == null },
                                calculateTableauToFreeCellPriority(columnIndex)
                            )
                        )
                    }
                }
            return moveSet
        } else {
            return emptyList()
        }
    }


    private fun findFreeCellToTableauMove(): List<FreeCellCardMove> {
        val moveSet = mutableListOf<FreeCellCardMove>()
        game.freeCells
            .forEachIndexed { freeCellIndex, freeCellCard ->
                game.tableau.forEachIndexed { columnIndex, column ->
                    if (freeCellCard != null && canStack(freeCellCard, column.lastOrNull())) {
                        moveSet.add(
                            FreeCellToTableauMove(
                                freeCellIndex,
                                columnIndex,
                                DestinationCardMove(freeCellCard, column.lastOrNull()),
                                calculateFreeCellToTableau(freeCellCard, column)
                            )
                        )
                    }
                }
            }
        return moveSet
    }


    private fun maxStackMove(): Int {
        return game.freeCells.count { it == null } + game.tableau.count { it.isEmpty() } + 1
    }

    private fun findCorrectStack(cardNumber: Int): List<TableauCardPosition> {
        val cardPositions = mutableListOf<TableauCardPosition>()
        game.tableau.forEachIndexed { columnIndex, cards ->
            if (cards.isNotEmpty() && cards.size >= cardNumber) {
                if (cardNumber == 1) {
                    cardPositions.add(TableauCardPosition(columnIndex, 1, cards.last()))
                } else {
                    var correct = true
                    var currentCard = cards.last()
                    var rowIndex = cardNumber
                    while (rowIndex > 0) {
                        val previousCard = cards[cards.size - rowIndex]
                        if (!canStack(currentCard, previousCard)) {
                            correct = false
                            break
                        }
                        currentCard = previousCard
                        rowIndex--
                    }

                    if (correct) {
                        cardPositions.add(TableauCardPosition(columnIndex - 1, cardNumber, currentCard))
                    }
                }
            }
        }
        return cardPositions
    }

    private fun findPossibleCanGo(from: List<TableauCardPosition>): List<FreeCellCardMove> {
        val moveSet = mutableSetOf<FreeCellCardMove>()
        val destination = game.tableau.map { it.lastOrNull() }
        for (sourceCard in from) {
            for ((destinationColumnIndex, destinationCard) in destination.withIndex()) {
                val notDuplicate = sourceCard.columnIndex != destinationColumnIndex
                val canStack = canStack(sourceCard.cardValue, destinationCard)
                val notJokingMove =
                    destinationCard != null && game.tableau[sourceCard.columnIndex].size != sourceCard.numberOfCard
                if (notDuplicate && canStack && notJokingMove) {
                    moveSet.add(
                        TableauToTableauMove(
                            sourceCard,
                            destinationColumnIndex,
                            DestinationCardMove(sourceCard.cardValue, destinationCard),
                            calculateTableauToTableauPriority(sourceCard, game.tableau[destinationColumnIndex])
                        )
                    )
                }
            }
        }
        return moveSet.toList()
    }


    private fun calculateHomePriority(columnIndex: Int): Int {
        return game.tableau[columnIndex].intersect(game.homeCell.getNextCard()).sumOf { it.rank.ordinal - 12 } * 10
    }

    private fun calculateTableauToFreeCellPriority(columnIndex: Int): Int {
        return calculateHomePriority(columnIndex) - 10
    }


    private fun calculateTableauToTableauPriority(sourceCard: TableauCardPosition, destinationColumn: List<Card>): Int {
        if (isFoundationTableau(sourceCard.cardValue, destinationColumn)) {
            return -100
        } else if (destinationColumn.isEmpty()) {
            return 30
        } else {
            return calculateHomePriority(sourceCard.columnIndex) - 10 - sourceCard.numberOfCard
        }
    }


    private fun calculateFreeCellToTableau(freeCellCard: Card, column: List<Card>): Int {
        if (isFoundationTableau(freeCellCard, column)) {
            return -200
        } else if (column.isEmpty()) {
            return 20
        } else {
            return 10
        }
    }

    private fun isFoundationTableau(sourceCard: Card, cards: List<Card>): Boolean {
        val makeFoundationTableau = sourceCard.rank == CardRank.KING && cards.isEmpty()
        val isFoundationTableau = isFoundationStack(cards)
        return makeFoundationTableau || isFoundationTableau
    }

    private fun isFoundationStack(cards: List<Card>): Boolean {
        if (cards.isNotEmpty()) {
            val firstCardColor = cards.first().suit.color
            var colorStackCheck = true
            for (card in cards) {
                val colorCheck = card.suit.color == firstCardColor
                if (colorCheck == colorStackCheck) {
                    colorStackCheck = !colorStackCheck
                } else {
                    return false
                }
            }
            return cards.mapIndexed { index, card -> card.rank.ordinal + index }.none { it != 12 }
        } else {
            return false
        }
    }

    companion object {
        fun canStack(current: Card, previous: Card?): Boolean {
            if (previous == null) {
                return true
            } else {
                return current.rank.ordinal == previous.rank.ordinal - 1 && current.suit.color != previous.suit.color
            }
        }

        fun canStack(cards: List<Card>): Boolean {
            val stack = cards.toMutableList()
            if (cards.size == 1) {
                return true
            }
            do {
                val current = stack.removeLast()
                val previous = stack.removeLast()
                if (!canStack(current, previous)) {
                    return false
                }
            } while (stack.size > 1)
            return true
        }
    }

}