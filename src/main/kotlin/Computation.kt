package org.example

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.example.filters.Scheme
import kotlin.math.min

val outName = { name: String, filter: String ->
    val (left, right) = name.split(".", limit = 2)
    left + "_" + filter + "." + right
}

object Computation {
    var dispatcher: CoroutineDispatcher = Dispatchers.Default

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
