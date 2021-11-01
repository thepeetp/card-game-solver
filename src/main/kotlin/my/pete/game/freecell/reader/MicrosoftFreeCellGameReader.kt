package my.pete.game.freecell.reader

import my.pete.common.Card
import my.pete.common.CardDeck
import my.pete.game.freecell.CapturedFreeCellGame
import my.pete.utils.MatchCardUtils
import java.nio.file.Path
import javax.imageio.ImageIO

class MicrosoftFreeCellGameReader(picturePath: Path) : FreeCellGameReader {

    private val captureWidth = 120
    private val captureHeight = 45
    private val tableauStartHorizontalIndex = 250
    private val tableauStartVerticalIndex = 350
    private val horizontalRange = 185
    private val verticalRange = 51
    private var verticalIndex = 0;
    private var horizontalIndex = 0;
    private val maxVerticalIndex = 7;
    private val maxHorizontalIndex = 8;
    private val image = ImageIO.read(picturePath.toFile())
    private val cardDeck = CardDeck()


    override fun read() = CapturedFreeCellGame(scanScreenShot())

    private fun scanScreenShot(): MutableList<MutableList<Card>> {
        val tableau = mutableListOf<MutableList<Card>>()
        for (horizontalIndex in horizontalIndex until maxHorizontalIndex) {
            val verticalStackCards = mutableListOf<Card>()
            for (verticalIndex in verticalIndex until maxVerticalIndex) {
                val card = scanCard(horizontalIndex, verticalIndex)
                if (card != null) {
                    verticalStackCards.add(cardDeck.pull(card))
                }
            }
            tableau.add(verticalStackCards)
        }
        return tableau
    }

    private fun scanCard(hIndex: Int, vIndex: Int): Card? {
        val captured = image.getSubimage(
            tableauStartHorizontalIndex + (hIndex * horizontalRange),
            tableauStartVerticalIndex + (vIndex * verticalRange),
            captureWidth,
            captureHeight
        )
        return MatchCardUtils.match(captured)
    }
}