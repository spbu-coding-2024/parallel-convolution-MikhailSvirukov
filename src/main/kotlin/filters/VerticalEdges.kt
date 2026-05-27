package org.example.filters

class VerticalEdges :
    Scheme(
        name = "vertical_edges",
        factor = 1.0,
        bias = 0.0,
        matrix =
            arrayOf(
                doubleArrayOf(0.0, 0.0, -1.0, 0.0, 0.0),
                doubleArrayOf(0.0, 0.0, -1.0, 0.0, 0.0),
                doubleArrayOf(0.0, 0.0, 4.0, 0.0, 0.0),
                doubleArrayOf(0.0, 0.0, -1.0, 0.0, 0.0),
                doubleArrayOf(0.0, 0.0, -1.0, 0.0, 0.0),
            ),
    )
