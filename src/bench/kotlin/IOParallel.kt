package org.example.benchmark

import kotlinx.coroutines.runBlocking
import org.example.executors.Executor
import org.example.executors.ExecutorManager
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
import java.io.File
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3)
@Measurement(iterations = 8)
open class IOParallel {
    val executor = ExecutorManager

    @Param("img/test")
    lateinit var dir: String

    @Param("blur")
    private lateinit var schemeName: String

    private lateinit var scheme: Scheme
    private lateinit var files: List<String>

    @Setup
    fun setup() =
        runBlocking {
            scheme = mapNameToScheme(schemeName)
            files = File(dir).listFiles()?.map { it.absolutePath } ?: emptyList()
        }

    @Benchmark
    fun sequential() {
        executor.executeSequentially(files, scheme) { name, scheme -> Executor.sequential(name, scheme) }
    }

    @Benchmark
    fun coroutinesRows() {
        ExecutorManager.executeWithCoroutine(
            files,
            scheme,
            JOBS,
        ) { name, scheme, jobs -> Executor.withCoroutinesRows(name, scheme, jobs) }
    }

    @Benchmark
    fun coroutinesColumns() {
        ExecutorManager.executeWithCoroutine(
            files,
            scheme,
            JOBS,
        ) { name, scheme, jobs -> Executor.withCoroutinesColumn(name, scheme, jobs) }
    }

    @Benchmark
    fun coroutinesSegment() {
        ExecutorManager.executeWithCoroutine(
            files,
            scheme,
            JOBS,
        ) { name, scheme, jobs -> Executor.withCoroutinesSegments(name, scheme, jobs) }
    }

    @Benchmark
    fun coroutinesChunks() {
        ExecutorManager.executeWithCoroutine(
            files,
            scheme,
            JOBS,
            JOBS,
        ) { name, scheme, x, y -> Executor.withCoroutinesChunk(name, scheme, x, y) }
    }

    @Benchmark
    fun coroutinesByPixel() {
        ExecutorManager.executeWithCoroutine(
            files,
            scheme,
            null,
            null,
        ) { name, scheme, x, y -> Executor.withCoroutinesChunk(name, scheme, x, y) }
    }
}
