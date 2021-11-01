package my.pete.common

import my.pete.common.CardColor.BLACK
import my.pete.common.CardColor.RED

enum class CardSuit(val color: CardColor, private val code: String) {
    CLUB(BLACK, "c"), DIAMOND(RED, "d"), HEART(RED, "h"), SPADE(BLACK, "s");

    companion object {
        fun fromCode(code: Char): CardSuit {
            for (value in values()) {
                if (value.code == code.toString()) {
                    return value
                }
            }
            throw Exception("Card Suit not found")
        }
    }
}