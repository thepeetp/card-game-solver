package my.pete.utils

import com.github.romankh3.image.comparison.ImageComparison
import my.pete.common.Card
import my.pete.common.CardRank
import my.pete.common.CardSuit
import java.awt.Color
import java.awt.Image
import java.awt.image.BufferedImage
import java.awt.image.PixelGrabber
import java.nio.file.Paths
import javax.imageio.ImageIO


object MatchCardUtils {

    fun match(actualImage: BufferedImage): Card? {
//        ImageIO.write(actualImage, "png", File("cards/${UUID.randomUUID()}.png"))
        if (!isValid(actualImage)) {
            return null
        }
        val resource = this.javaClass.classLoader.getResource("cards")
        val expectedCardImages = Paths.get(resource.file.removePrefix("/")).toFile().listFiles()
        var minDiff = 99.0f
        var selectedCard: Card? = null
        for (expectedCardImage in expectedCardImages) {
            val result = ImageComparison(ImageIO.read(expectedCardImage), actualImage).compareImages()
            val differencePercent = result.differencePercent
            if (differencePercent < 4 && differencePercent < minDiff) {
                minDiff = differencePercent
                val code = expectedCardImage.canonicalFile.name.removeSuffix(".png")
                selectedCard = Card(CardRank.fromCode(code.first()), CardSuit.fromCode(code.last()))
            }
        }
        return selectedCard
    }


    fun isValid(img: Image): Boolean {
        //img = img.getScaledInstance(100, -1, Image.SCALE_FAST);
        val w: Int = img.getWidth(null)
        val h: Int = img.getHeight(null)
        val pixels = IntArray(w * h)
        val pg = PixelGrabber(img, 0, 0, w, h, pixels, 0, w)
        pg.grabPixels()
        var isValid = false
        for (pixel in pixels) {
            val color = Color(pixel)
            if (color.getAlpha() === 0 || color.getRGB() !== Color.WHITE.getRGB()) {
                isValid = true
                break
            }
        }
        return isValid
    }
}