package org.example.filters

class Emboss :
    Scheme(
        name = "emboss",
        factor = 1.0,
        bias = 128.0,
        matrix =
            arrayOf(
                doubleArrayOf(-1.0, -1.0, 0.0),
                doubleArrayOf(-1.0, 0.0, 1.0),
                doubleArrayOf(0.0, 1.0, 1.0),
            ),
    )
