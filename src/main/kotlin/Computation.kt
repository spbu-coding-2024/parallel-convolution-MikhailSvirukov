package org.example

import org.example.filters.Scheme

val outName = { name: String, filter: String ->
    val (left, right) = name.split(".", limit = 2)
    left + "_" + filter + "." + right
}

object Computation {
    fun sequential(
        image: LoadedImage,
        filter: Scheme,
    ): ByteArray {
        val output = ByteArray(image.width * image.height * image.channels)

        Convolution.applyRange(
            0 until image.width * image.height,
            image.width,
            image.height,
            image.input,
            output,
            filter,
        )
        return output
    }
}
