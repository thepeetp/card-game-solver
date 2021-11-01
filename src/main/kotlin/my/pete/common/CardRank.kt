package my.pete.common

enum class CardRank(private val code: String) {
    ACE("a"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    TEN("t"),
    JACK("j"),
    QUEEN("q"),
    KING("k");


    companion object {
        fun fromCode(code: Char): CardRank {
            for (value in values()) {
                if (value.code == code.toString()) {
                    return value
                }
            }
            throw Exception("Card Rank not found")
        }
    }

}