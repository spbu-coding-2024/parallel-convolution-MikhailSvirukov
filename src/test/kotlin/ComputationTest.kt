package org.example

import kotlinx.coroutines.runBlocking
import org.example.filters.Id
import org.example.filters.Scheme
import org.example.filters.allSchemes
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.nio.file.Files
import java.util.stream.Stream
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.test.assertContentEquals

class ComputationTest {
    @ParameterizedTest(name = "{0}")
    @MethodSource("imageFilterCases")
    fun `id filter keeps source image unchanged`(imagePath: String) =
        runBlocking {
            val image = IOManager.loadRgbImage(imagePath)
            val output = Computation.sequential(image, Id())
            val outputRow = Computation.withCoroutinesRows(image, Id(), null)
            val outputCol = Computation.withCoroutinesColumn(image, Id(), null)
            val outputSeg = Computation.withCoroutinesSegments(image, Id(), JOBS)
            val outputChunk =
                Computation.withCoroutinesChunk(
                    image,
                    Id(),
                    JOBS,
                    JOBS,
                )
            assertContentEquals(image.input, output, "image: $imagePath sequentially")
            assertContentEquals(image.input, outputRow, "image: $imagePath by rows")
            assertContentEquals(image.input, outputCol, "image: $imagePath by columns")
            assertContentEquals(image.input, outputSeg, "image: $imagePath by segments")
            assertContentEquals(image.input, outputChunk, "image: $imagePath by chunks")
        }

    @ParameterizedTest(name = "{0} x {1}")
    @MethodSource("imageFilterCases")
    fun `withCoroutinesSegments matches sequential output`(
        imagePath: String,
        filter: Scheme,
    ) = runBlocking {
        val image = IOManager.loadRgbImage(imagePath)
        val reference = Computation.sequential(image, filter)

        val segments = Computation.withCoroutinesSegments(image, filter, JOBS)

        assertContentEquals(reference, segments)
    }

    @ParameterizedTest(name = "{0} x {1}")
    @MethodSource("imageFilterCases")
    fun `withCoroutinesColumn matches sequential output`(
        imagePath: String,
        filter: Scheme,
    ) = runBlocking {
        val image = IOManager.loadRgbImage(imagePath)
        val reference = Computation.sequential(image, filter)

        val columns = Computation.withCoroutinesColumn(image, filter, null)

        assertContentEquals(reference, columns)
    }

    @ParameterizedTest(name = "{0} x {1}")
    @MethodSource("imageFilterCases")
    fun `withCoroutinesRows matches sequential output`(
        imagePath: String,
        filter: Scheme,
    ) = runBlocking {
        val image = IOManager.loadRgbImage(imagePath)
        val reference = Computation.sequential(image, filter)

        val rows = Computation.withCoroutinesRows(image, filter, null)

        assertContentEquals(reference, rows)
    }

    @ParameterizedTest(name = "{0} x {1}")
    @MethodSource("imageFilterCases")
    fun `withCoroutinesChunk matches sequential output`(
        imagePath: String,
        filter: Scheme,
    ) = runBlocking {
        val image = IOManager.loadRgbImage(imagePath)
        val reference = Computation.sequential(image, filter)

        val chunks = Computation.withCoroutinesChunk(image, filter, JOBS, JOBS)

        assertContentEquals(reference, chunks)
    }

    @ParameterizedTest(name = "{0} x {1} with enlarging filter by {2}")
    @MethodSource("enlargeFilterCases")
    fun `apply filer with zeroes`(
        imagePath: String,
        filter: Scheme,
        enlarge: Int,
    ) {
        val image = IOManager.loadRgbImage(imagePath)
        val enlargedScheme = fillMatrixWithZeroes(filter, enlarge)
        val reference = Computation.sequential(image, filter)

        val enlarged = Computation.sequential(image, enlargedScheme)

        assertContentEquals(reference, enlarged)
    }

    companion object {
        private class TestScheme(
            name: String,
            factor: Double,
            bias: Double,
            matrix: Array<DoubleArray>,
        ) : Scheme(name, factor, bias, matrix)

        private const val JOBS = 8
        private const val DIRPATH = "img/test"
        private const val MAX_ENLARGE_COEF = 10
        private const val MAX_TESTS = 42L
        private val all = allSchemes()

        fun imagePaths(): List<String> =
            Files.list(File(DIRPATH).toPath()).use { paths ->
                paths
                    .map { it.toString() }
                    .toList()
            }

        fun fillMatrixWithZeroes(
            scheme: Scheme,
            coef: Int,
        ): Scheme {
            val old = scheme.matrix
            val oldSize = old.size
            val newSize = oldSize + 2 * coef

            val newMatrix = Array(newSize) { DoubleArray(newSize) }

            for (i in 0 until oldSize) {
                for (j in 0 until oldSize) {
                    newMatrix[i + coef][j + coef] = old[i][j]
                }
            }

            return TestScheme(scheme.name, scheme.factor, scheme.bias, newMatrix)
        }

        @JvmStatic
        fun imageFilterCases(): Stream<Arguments> =
            imagePaths()
                .stream()
                .flatMap { imagePath ->
                    all.stream().map { filter ->
                        Arguments.of(imagePath, filter)
                    }
                }

        @JvmStatic
        fun enlargeFilterCases(): Stream<Arguments> =
            Stream
                .generate {
                    val img = imagePaths().random()
                    val scheme = all.random()
                    val size = Random.nextInt(1..MAX_ENLARGE_COEF)
                    Arguments.of(img, scheme, size)
                }.limit(MAX_TESTS)
    }
}
