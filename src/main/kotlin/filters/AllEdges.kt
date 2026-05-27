package org.example.filters

class AllEdges :
    Scheme(
        name = "all_edges",
        factor = 1.0,
        bias = 0.0,
        matrix =
            arrayOf(
                doubleArrayOf(-1.0, -1.0, -1.0),
                doubleArrayOf(-1.0, 8.0, -1.0),
                doubleArrayOf(-1.0, -1.0, -1.0),
            ),
    )
