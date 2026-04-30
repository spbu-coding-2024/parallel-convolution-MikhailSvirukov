package org.example

import org.example.filters.Scheme

class Executor {
    suspend fun sequential(
        name: String,
        filter: Scheme,
    ) {
        val image = IOManager.loadRgbImage(name)
        val output = Computation.sequential(image, filter)
        IOManager.saveRgbImage(outName.invoke(name, filter.name), image.width, image.height, output)
    }
}
