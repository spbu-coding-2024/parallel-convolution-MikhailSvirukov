package org.example.filters

class HorizontalEdges :
    Scheme(
        name = "horizontal_edges",
        factor = 1.0,
        bias = 0.0,
        matrix =
            arrayOf(
                doubleArrayOf(0.0, 0.0, -1.0, 0.0, 0.0),
                doubleArrayOf(0.0, 0.0, -1.0, 0.0, 0.0),
                doubleArrayOf(0.0, 0.0, 2.0, 0.0, 0.0),
                doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0),
                doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0),
            ),
    )
