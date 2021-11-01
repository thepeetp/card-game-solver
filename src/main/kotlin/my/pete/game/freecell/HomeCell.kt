package my.pete.game.freecell

import my.pete.common.Card
import my.pete.common.CardRank
import my.pete.common.CardRank.ACE
import my.pete.common.CardSuit
import my.pete.common.CardSuit.*

class HomeCell {
    val stack: MutableMap<CardSuit, CardRank?> = mutableMapOf(
        CLUB to null,
        DIAMOND to null,
        HEART to null,
        SPADE to null
    )

    fun available(card: Card): Boolean {
        val currentHomeRank = stack[card.suit]
        if (card.rank == ACE) {
            return true
        } else if (currentHomeRank != null && card.rank.ordinal == currentHomeRank.ordinal + 1) {
            return true
        }
        return false
    }

    fun getNextCard(): List<Card> {
        val cards = mutableListOf<Card>()
        stack.entries.forEach { (suit, rank) ->
            if (rank == null) {
                cards.add(Card(ACE, suit))
            } else if (rank.ordinal + 1 < CardRank.values().size) {
                cards.add(Card(CardRank.values()[rank.ordinal + 1], suit))
            }
        }
        return cards
    }

    fun getPriorityNextCard(): Card {
        return getNextCard().minByOrNull { it.rank.ordinal }!!
    }

    fun deposit(card: Card) {
        stack[card.suit] = card.rank
    }

    fun isFull() = stack.values.all { it == CardRank.KING }

    fun getScore() = stack.values.filterNotNull().sumOf { it.ordinal + 1 }
}