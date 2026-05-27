package org.example

import org.example.filters.Scheme

class Convolution {
    companion object {
        fun applyRange(
            range: IntRange,
            width: Int,
            height: Int,
            input: ByteArray,
            output: ByteArray,
            filter: Scheme,
        ) {
            val fw = filter.matrix[0].size
            val fh = filter.matrix.size

            for (n in range) {
                val x = n % width
                val y = n / width

                var accR = 0.0
                var accG = 0.0
                var accB = 0.0

                for (fy in 0 until fh) {
                    for (fx in 0 until fw) {
                        val ix = (x - fw / 2 + fx + width) % width
                        val iy = (y - fh / 2 + fy + height) % height

                        val idx = (iy * width + ix) * 3

                        val b = input[idx].toUByte().toInt()
                        val g = input[idx + 1].toUByte().toInt()
                        val r = input[idx + 2].toUByte().toInt()

                        val k = filter.matrix[fy][fx]

                        accR += r * k
                        accG += g * k
                        accB += b * k
                    }
                }

                val outIdx = (y * width + x) * 3

                output[outIdx] = (filter.factor * accB + filter.bias).toInt().coerceIn(0, 255).toByte()
                output[outIdx + 1] = (filter.factor * accG + filter.bias).toInt().coerceIn(0, 255).toByte()
                output[outIdx + 2] = (filter.factor * accR + filter.bias).toInt().coerceIn(0, 255).toByte()
            }
        }

        fun applyChunk(
            xRange: IntRange,
            yRange: IntRange,
            width: Int,
            height: Int,
            input: ByteArray,
            output: ByteArray,
            filter: Scheme,
        ) {
            val fw = filter.matrix[0].size
            val fh = filter.matrix.size

            for (y in yRange) {
                for (x in xRange) {
                    var accR = 0.0
                    var accG = 0.0
                    var accB = 0.0

                    for (fy in 0 until fh) {
                        for (fx in 0 until fw) {
                            val ix = (x - fw / 2 + fx + width) % width
                            val iy = (y - fh / 2 + fy + height) % height

                            val idx = (iy * width + ix) * 3

                            val b = input[idx].toUByte().toInt()
                            val g = input[idx + 1].toUByte().toInt()
                            val r = input[idx + 2].toUByte().toInt()

                            val k = filter.matrix[fy][fx]

                            accR += r * k
                            accG += g * k
                            accB += b * k
                        }
                    }

                    val outIdx = (y * width + x) * 3

                    output[outIdx] = (filter.factor * accB + filter.bias).toInt().coerceIn(0, 255).toByte()
                    output[outIdx + 1] = (filter.factor * accG + filter.bias).toInt().coerceIn(0, 255).toByte()
                    output[outIdx + 2] = (filter.factor * accR + filter.bias).toInt().coerceIn(0, 255).toByte()
                }
            }
        }
    }
}
