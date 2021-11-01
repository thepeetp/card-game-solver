package my.pete.common

data class Card(val rank: CardRank, val suit: CardSuit) {
    override fun hashCode(): Int {
        return rank.hashCode() + suit.hashCode()
    }
}
