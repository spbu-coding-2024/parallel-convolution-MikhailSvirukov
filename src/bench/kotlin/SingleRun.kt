package org.example.benchmark

import kotlinx.coroutines.runBlocking
import org.example.Computation
import org.example.IOManager
import org.example.LoadedImage
import org.example.filters.Scheme
import org.example.filters.mapNameToScheme
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.Warmup

@State(Scope.Benchmark)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(java.util.concurrent.TimeUnit.MILLISECONDS)
@Warmup(iterations = 3)
@Measurement(iterations = 8)
open class SingleRun {
    @Param("img/bench/snow.jpg", "img/bench/ktulhu.jpg")
    lateinit var img: String

    @Param("blur")
    private lateinit var schemeName: String

    private lateinit var scheme: Scheme
    private lateinit var loadedImage: LoadedImage

    @Setup
    fun setup() =
        runBlocking {
            scheme = mapNameToScheme(schemeName)
            loadedImage = IOManager.loadRgbImage(img)
        }

    @Benchmark
    fun sequential() {
        Computation.sequential(loadedImage, scheme)
    }
}
