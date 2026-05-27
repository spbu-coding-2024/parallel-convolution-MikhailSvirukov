package org.example.filters

class DegreeEdges :
    Scheme(
        name = "degree",
        factor = 1.0,
        bias = 0.0,
        matrix =
            arrayOf(
                doubleArrayOf(-1.0, 0.0, 0.0, 0.0, 0.0),
                doubleArrayOf(0.0, -2.0, 0.0, 0.0, 0.0),
                doubleArrayOf(0.0, 0.0, 6.0, 0.0, 0.0),
                doubleArrayOf(0.0, 0.0, 0.0, -2.0, 0.0),
                doubleArrayOf(0.0, 0.0, 0.0, 0.0, -1.0),
            ),
    )
