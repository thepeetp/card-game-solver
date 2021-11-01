package my.pete.common

class CardDeck {
    private val cards: MutableList<Card> = mutableListOf()

    init {
        CardSuit.values().forEach { suit ->
            CardRank.values().forEach { rank ->
                cards.add(Card(rank = rank, suit = suit))
            }
        }
    }

    fun shuffle() {
        cards.shuffle()
    }

    fun pull(): Card {
        return cards.removeFirst()
    }

    fun pull(card: Card): Card {
        if (!cards.remove(card)) {
            throw Exception("Card is not in deck")
        }
        return card
    }

    fun isEmpty() = cards.isEmpty()

    fun pick(card: Card) {
        if (!cards.remove(card)) {
            throw Exception("Card not found in deck")
        }
    }
}