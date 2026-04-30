package org.example.filters

open class Scheme(
    val name: String,
    val factor: Double,
    val bias: Double,
    val matrix: Array<DoubleArray>,
)

fun allSchemes() =
    listOf(
        Id(),
        Zero(),
        Mean(),
        Blur(),
        ExcessiveEdges(),
        HorizontalEdges(),
        VerticalEdges(),
        Emboss(),
        Sharpen1(),
        Sharpen2(),
        DegreeEdges(),
    )

fun mapNameToScheme(key: String) = allSchemes().find { it.name == key } ?: error("Unknown scheme: $key")
