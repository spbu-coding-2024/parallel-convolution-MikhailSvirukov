@file:OptIn(ExperimentalCli::class)

package org.example

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.optional
import org.example.executors.Executor
import org.example.executors.ExecutorManager
import org.example.filters.allSchemes
import org.example.filters.mapNameToScheme
import java.io.File

val schemeArgType =
    ArgType.Choice(
        choices = allSchemes(),
        toVariant = { mapNameToScheme(it) },
        variantToString = { it.name },
    )

fun main(args: Array<String>) {
    class CoroutinesChunks : Subcommand("coroutines_chunks", "Run coroutine chunk computation") {
        private val filename by argument(ArgType.String, "path to file")
        private val filter by argument(schemeArgType, "filter type")
        private val tasksX by argument(ArgType.Int, "number of tasks to split for rows").optional()
        private val tasksY by argument(ArgType.Int, "number of tasks to split for columns").optional()

        override fun execute() {
            val file = File(filename)
            require(file.exists()) { "File does not exist: $filename" }
            if (file.isFile) {
                ExecutorManager.executeWithCoroutine(
                    filename,
                    filter,
                    tasksX,
                    tasksY,
                ) { name, scheme, x, y -> Executor.withCoroutinesChunk(name, scheme, x, y) }
            } else {
                val files = file.listFiles()?.map { it.absolutePath } ?: emptyList()
                ExecutorManager.executeWithCoroutine(
                    files,
                    filter,
                    tasksX,
                    tasksY,
                ) { name, scheme, x, y -> Executor.withCoroutinesChunk(name, scheme, x, y) }
            }
        }
    }

    class Sequential : Subcommand("sequential", "Run sequential computation") {
        private val filename by argument(ArgType.String, "path to file")
        private val filter by argument(schemeArgType, "filter type")

        override fun execute() {
            val file = File(filename)
            require(file.exists()) { "File does not exist: $filename" }
            if (file.isFile) {
                ExecutorManager.executeSequentially(filename, filter) { name, scheme -> Executor.sequential(name, scheme) }
            } else {
                val files = file.listFiles()?.map { it.absolutePath } ?: emptyList()
                ExecutorManager.executeSequentially(files, filter) { name, scheme -> Executor.sequential(name, scheme) }
            }
        }
    }

    class CoroutinesRows : Subcommand("coroutines_rows", "Run coroutine rows computation") {
        private val filename by argument(ArgType.String, "path to file")
        private val filter by argument(schemeArgType, "filter type")
        private val tasks by argument(ArgType.Int, "number of tasks to split file").optional()

        override fun execute() {
            val file = File(filename)
            require(file.exists()) { "File does not exist: $filename" }
            if (file.isFile) {
                ExecutorManager.executeWithCoroutine(
                    filename,
                    filter,
                    tasks,
                ) { name, scheme, jobs -> Executor.withCoroutinesRows(name, scheme, jobs) }
            } else {
                val files = file.listFiles()?.map { it.absolutePath } ?: emptyList()
                ExecutorManager.executeWithCoroutine(
                    files,
                    filter,
                    tasks,
                ) { name, scheme, jobs -> Executor.withCoroutinesRows(name, scheme, jobs) }
            }
        }
    }

    class CoroutinesColumns : Subcommand("coroutines_columns", "Run coroutine columns computation") {
        private val filename by argument(ArgType.String, "path to file")
        private val filter by argument(schemeArgType, "filter type")
        private val tasks by argument(ArgType.Int, "number of tasks to split file").optional()

        override fun execute() {
            val file = File(filename)
            require(file.exists()) { "File does not exist: $filename" }
            if (file.isFile) {
                ExecutorManager.executeWithCoroutine(
                    filename,
                    filter,
                    tasks,
                ) { name, scheme, jobs -> Executor.withCoroutinesColumn(name, scheme, jobs) }
            } else {
                val files = file.listFiles()?.map { it.absolutePath } ?: emptyList()
                ExecutorManager.executeWithCoroutine(
                    files,
                    filter,
                    tasks,
                ) { name, scheme, jobs -> Executor.withCoroutinesColumn(name, scheme, jobs) }
            }
        }
    }

    class CoroutinesSegment : Subcommand("coroutines_segment", "Run coroutine segment computation") {
        private val filename by argument(ArgType.String, "path to file")
        private val filter by argument(schemeArgType, "filter type")
        private val tasks by argument(ArgType.Int, "number of tasks to split file").optional()

        override fun execute() {
            val file = File(filename)
            require(file.exists()) { "File does not exist: $filename" }
            if (file.isFile) {
                ExecutorManager.executeWithCoroutine(
                    filename,
                    filter,
                    tasks,
                ) { name, scheme, jobs -> Executor.withCoroutinesSegments(name, scheme, jobs) }
            } else {
                val files = file.listFiles()?.map { it.absolutePath } ?: emptyList()
                ExecutorManager.executeWithCoroutine(
                    files,
                    filter,
                    tasks,
                ) { name, scheme, jobs -> Executor.withCoroutinesSegments(name, scheme, jobs) }
            }
        }
    }

    val parser = ArgParser("convolution")

    parser.subcommands(
        Sequential(),
        CoroutinesRows(),
        CoroutinesColumns(),
        CoroutinesSegment(),
        CoroutinesChunks(),
    )
    parser.parse(args)
}
