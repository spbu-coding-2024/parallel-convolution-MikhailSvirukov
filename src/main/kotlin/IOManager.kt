package org.example

import boofcv.io.image.ConvertBufferedImage
import boofcv.io.image.UtilImageIO
import boofcv.struct.image.GrayU8
import boofcv.struct.image.ImageType
import boofcv.struct.image.Planar

class LoadedImage(
    val width: Int,
    val height: Int,
    val channels: Int,
    val input: ByteArray,
)

object IOManager {
    fun loadRgbImage(name: String): LoadedImage {
        val buffered = UtilImageIO.loadImage(name) ?: error("Cannot load image: $name")

        val width = buffered.width
        val height = buffered.height

        val imageType = ImageType.pl(3, GrayU8::class.java)

        val planar: Planar<GrayU8> =
            ConvertBufferedImage.convertFrom(buffered, true, imageType)

        val channels = planar.numBands
        val input = ByteArray(width * height * channels)

        var idx = 0
        for (y in 0 until height) {
            for (x in 0 until width) {
                for (c in 0 until channels) {
                    input[idx++] = planar.getBand(c).unsafe_get(x, y).toByte()
                }
            }
        }

        return LoadedImage(width, height, channels, input)
    }

    fun saveRgbImage(
        outName: String,
        width: Int,
        height: Int,
        output: ByteArray,
    ) {
        val planar = Planar(GrayU8::class.java, width, height, 3)

        var idx = 0
        for (y in 0 until height) {
            for (x in 0 until width) {
                for (c in 0 until 3) {
                    planar.getBand(c).unsafe_set(x, y, output[idx++].toInt() and 0xFF)
                }
            }
        }

        val buffered = ConvertBufferedImage.convertTo(planar, null, true)
        UtilImageIO.saveImage(buffered, outName)
    }
}
