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

    suspend fun withCoroutinesSegments(
        image: LoadedImage,
        filter: Scheme,
        numJobs: Int?,
    ): ByteArray {
        val output = ByteArray(image.width * image.height * image.channels)
        val total = image.width * image.height
        val jobs = numJobs ?: total
        val batchSize = (total + jobs - 1) / jobs
        coroutineScope {
            val jobs =
                (0 until jobs).map { i ->
                    launch(dispatcher) {
                        val start = i * batchSize
                        val end = min(start + batchSize, total)
                        Convolution.applyRange(
                            start until end,
                            image.width,
                            image.height,
                            image.input,
                            output,
                            filter,
                        )
                    }
                }

            jobs.joinAll()
        }
        return output
    }

    suspend fun withCoroutinesColumn(
        image: LoadedImage,
        filter: Scheme,
        numJobs: Int?,
    ): ByteArray {
        val output = ByteArray(image.width * image.height * image.channels)

        val jobs = numJobs ?: image.width
        val tasks = (image.width + jobs - 1) / jobs

        coroutineScope {
            val jobsList =
                (0 until jobs).map { i ->
                    launch(dispatcher) {
                        val start = i * tasks
                        val end = min(start + tasks, image.width)
                        Convolution.applyChunk(
                            start until end,
                            0 until image.height,
                            image.width,
                            image.height,
                            image.input,
                            output,
                            filter,
                        )
                    }
                }

            jobsList.joinAll()
        }
        return output
    }

    suspend fun withCoroutinesRows(
        image: LoadedImage,
        filter: Scheme,
        numJobs: Int?,
    ): ByteArray {
        val output = ByteArray(image.width * image.height * image.channels)

        val jobs = numJobs ?: image.height
        val tasks = (image.height + jobs - 1) / jobs

        coroutineScope {
            val jobsList =
                (0 until jobs).map { i ->
                    launch(dispatcher) {
                        val start = i * tasks
                        val end = min(start + tasks, image.height)
                        Convolution.applyChunk(
                            0 until image.width,
                            start until end,
                            image.width,
                            image.height,
                            image.input,
                            output,
                            filter,
                        )
                    }
                }

            jobsList.joinAll()
        }
        return output
    }

    suspend fun withCoroutinesChunk(
        image: LoadedImage,
        filter: Scheme,
        numJobsX: Int?,
        numJobsY: Int?,
    ): ByteArray {
        val output = ByteArray(image.width * image.height * image.channels)

        val jobsx = numJobsX ?: image.width
        val jobsy = numJobsY ?: image.height

        val batchSizeX = (image.width + jobsx - 1) / jobsx
        val batchSizeY = (image.height + jobsy - 1) / jobsy

        coroutineScope {
            val jobs =
                buildList {
                    for (i in 0 until jobsx) {
                        for (j in 0 until jobsy) {
                            add(
                                launch(dispatcher) {
                                    val startX = i * batchSizeX
                                    val endX = min((i + 1) * batchSizeX, image.width)
                                    val startY = j * batchSizeY
                                    val endY = min((j + 1) * batchSizeY, image.height)
                                    Convolution.applyChunk(
                                        startX until endX,
                                        startY until endY,
                                        image.width,
                                        image.height,
                                        image.input,
                                        output,
                                        filter,
                                    )
                                },
                            )
                        }
                    }
                }

            jobs.joinAll()
        }
        return output
    }
}
