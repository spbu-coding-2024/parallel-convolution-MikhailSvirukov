@file:OptIn(ExperimentalCli::class)

package org.example

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.coroutines.runBlocking
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

    class Sequential : Subcommand("sequential", "Run sequential computation") {
        private val filename by argument(ArgType.String, "path to file")
        private val filter by argument(schemeArgType, "filter type")

        override fun execute() = runBlocking {
            val file = File(filename)
            require(file.exists()) { "File does not exist: $filename" }
            Executor.sequential(filename, filter)
        }
    }

    val parser = ArgParser("convolution")

    parser.subcommands(
        Sequential(),
    )
    parser.parse(args)
}