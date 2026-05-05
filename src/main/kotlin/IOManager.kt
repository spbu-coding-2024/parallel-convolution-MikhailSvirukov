package org.example

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs

class LoadedImage(
    val width: Int,
    val height: Int,
    val channels: Int,
    val input: ByteArray,
)

object IOManager {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    init {
        System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME)
    }

    suspend fun loadRgbImage(name: String): LoadedImage =
        withContext(dispatcher) {
            val src = Imgcodecs.imread(name)
            val width = src.cols()
            val height = src.rows()
            val channels = src.channels()

            val input = ByteArray(width * height * channels)
            src.get(0, 0, input)

            LoadedImage(width, height, channels, input)
        }

    suspend fun saveRgbImage(
        outName: String,
        width: Int,
        height: Int,
        output: ByteArray,
    ) = withContext(dispatcher) {
        val result = Mat(height, width, CvType.CV_8UC3)
        result.put(0, 0, output)
        Imgcodecs.imwrite(outName, result)
    }
}
