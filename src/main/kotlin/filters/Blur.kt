package org.example.filters

class Blur :
    Scheme(
        name = "blur",
        factor = 1.0,
        bias = 0.0,
        matrix =
            arrayOf(
                doubleArrayOf(0.0, 0.2, 0.0),
                doubleArrayOf(0.2, 0.2, 0.2),
                doubleArrayOf(0.0, 0.2, 0.0),
            ),
    )
