package org.example.filters

class Sharpen2 :
    Scheme(
        name = "sharpen2",
        factor = 1.0 / 8.0,
        bias = 0.0,
        matrix =
            arrayOf(
                doubleArrayOf(-1.0, -1.0, -1.0, -1.0, -1.0),
                doubleArrayOf(-1.0, 2.0, 2.0, 2.0, -1.0),
                doubleArrayOf(-1.0, 2.0, 8.0, 2.0, -1.0),
                doubleArrayOf(-1.0, 2.0, 2.0, 2.0, -1.0),
                doubleArrayOf(-1.0, -1.0, -1.0, -1.0, -1.0),
            ),
    )
