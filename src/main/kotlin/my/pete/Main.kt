package my.pete

import my.pete.game.freecell.reader.MicrosoftFreeCellGameReader
import java.nio.file.Paths

fun main() {
    val freeCellReader = MicrosoftFreeCellGameReader(Paths.get("C:\\Users\\USER\\Pictures\\Screenshots\\sample_j.png"))
    val game = freeCellReader.read().findBestWinGame()
    print(game)
}