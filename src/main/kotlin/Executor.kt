package org.example

import org.example.filters.Scheme

object Executor {
    fun sequential(
        name: String,
        filter: Scheme,
    ) {
        val image = IOManager.loadRgbImage(name)
        val output = Computation.sequential(image, filter)
        IOManager.saveRgbImage(outName.invoke(name, filter.name), image.width, image.height, output)
    }

    suspend fun withCoroutinesSegments(
        name: String,
        filter: Scheme,
        numJobs: Int?,
    ) {
        val image = IOManager.loadRgbImage(name)
        val output = Computation.withCoroutinesSegments(image, filter, numJobs)
        IOManager.saveRgbImage(outName.invoke(name, filter.name), image.width, image.height, output)
    }

    suspend fun withCoroutinesColumn(
        name: String,
        filter: Scheme,
        numJobs: Int?,
    ) {
        val image = IOManager.loadRgbImage(name)
        val output = Computation.withCoroutinesColumn(image, filter, numJobs)
        IOManager.saveRgbImage(outName.invoke(name, filter.name), image.width, image.height, output)
    }

    suspend fun withCoroutinesRows(
        name: String,
        filter: Scheme,
        numJobs: Int?,
    ) {
        val image = IOManager.loadRgbImage(name)
        val output = Computation.withCoroutinesRows(image, filter, numJobs)
        IOManager.saveRgbImage(outName.invoke(name, filter.name), image.width, image.height, output)
    }

    suspend fun withCoroutinesChunk(
        name: String,
        filter: Scheme,
        numJobsX: Int?,
        numJobsY: Int?,
    ) {
        val image = IOManager.loadRgbImage(name)
        val output = Computation.withCoroutinesChunk(image, filter, numJobsX, numJobsY)
        IOManager.saveRgbImage(outName.invoke(name, filter.name), image.width, image.height, output)
    }
}
