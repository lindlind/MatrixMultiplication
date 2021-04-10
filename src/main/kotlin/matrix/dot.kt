package matrix

import matrix.Matrix.Companion.emptyMatrix
import java.lang.IllegalArgumentException
import kotlin.math.ceil
import kotlin.math.log2
import kotlin.math.min
import kotlin.math.pow

typealias ChunkedMatrix = Array<Array<Matrix>>

fun emptyChunkedMatrix(): ChunkedMatrix = Array(2) { Array(2) { Matrix.emptyMatrix() } }

operator fun Matrix.times(other: Matrix): Matrix {
    if (this.columns != other.rows) throw IllegalArgumentException("Invalid size of matrices")
    return dot(this, other)
}

private fun dot(matrixA: Matrix, matrixB: Matrix): Matrix {

    if (matrixA.isEmpty() || matrixB.isEmpty()) return emptyMatrix()

    val maxSideSize = maxOf(matrixA.rows, matrixA.columns, matrixB.rows, matrixB.columns)
    val chunkRank = ceil(log2(maxSideSize.toDouble()))
    if (chunkRank <= 2) return dotSimple(matrixA, matrixB)
    val chunkSize = (2.0).pow(chunkRank).toInt()

    val chunkedA = toChunked(matrixA, chunkSize / 2)
    val chunkedB = toChunked(matrixB, chunkSize / 2)

    val chunkedC = when {
        chunkedA.flatten().any { it.isEmpty() } -> dotChunked(chunkedA, chunkedB)
        chunkedB.flatten().any { it.isEmpty() } -> dotChunked(chunkedA, chunkedB)
        else -> dotStrassen(chunkedA, chunkedB)
    }

    return fromChunked(chunkedC)
}

private fun dotStrassen(matrixA: ChunkedMatrix, matrixB: ChunkedMatrix): ChunkedMatrix {
    //if (chunkSize < 2) return dotSimple(matrixA, matrixB)
    val matrixP1 = dot(matrixA[0][0] + matrixA[1][1], matrixB[0][0] + matrixB[1][1])
    val matrixP2 = dot(matrixA[1][0] + matrixA[1][1], matrixB[0][0])
    val matrixP3 = dot(matrixA[0][0], matrixB[0][1] - matrixB[1][1])
    val matrixP4 = dot(matrixA[1][1], matrixB[1][0] - matrixB[0][0])
    val matrixP5 = dot(matrixA[0][0] + matrixA[0][1], matrixB[1][1])
    val matrixP6 = dot(matrixA[1][0] - matrixA[0][0], matrixB[0][0] + matrixB[0][1])
    val matrixP7 = dot(matrixA[0][1] - matrixA[1][1], matrixB[1][0] + matrixB[1][1])

    val matrixC = emptyChunkedMatrix()
    matrixC[0][0] = matrixP1 + matrixP4 - matrixP5 + matrixP7
    matrixC[0][1] = matrixP3 + matrixP5
    matrixC[1][0] = matrixP2 + matrixP4
    matrixC[1][1] = matrixP1 - matrixP2 + matrixP3 + matrixP6

    return matrixC
}

private fun dotChunked(matrixA: ChunkedMatrix, matrixB: ChunkedMatrix): ChunkedMatrix {
    //if (chunkSize < 2) return dotSimple(matrixA, matrixB)
    val matrixC = emptyChunkedMatrix()
    matrixC[0][0] = dot(matrixA[0][0], matrixB[0][0]) + dot(matrixA[0][1], matrixB[1][0])
    matrixC[0][1] = dot(matrixA[0][0], matrixB[0][1]) + dot(matrixA[0][1], matrixB[1][1])
    matrixC[1][0] = dot(matrixA[1][0], matrixB[0][0]) + dot(matrixA[1][1], matrixB[1][0])
    matrixC[1][1] = dot(matrixA[1][0], matrixB[0][1]) + dot(matrixA[1][1], matrixB[1][1])
    return matrixC
}

fun dotSimple(matrixA: Matrix, matrixB: Matrix): Matrix {
    val matrixC = Matrix(matrixA.rows, matrixB.columns)
    for (i in 0 until matrixA.rows) {
        for (k in 0 until matrixB.columns) {
            for (j in 0 until min(matrixA.columns, matrixB.rows)) {
                matrixC[i, k] += matrixA[i, j] * matrixB[j, k]
            }
        }
    }
    return matrixC
}

private fun toChunked(matrix: Matrix, chunkSize: Int): ChunkedMatrix {
    val chunked = emptyChunkedMatrix()
    chunked[0][0] = matrix.slice(0 until chunkSize, 0 until chunkSize)
    chunked[0][1] = matrix.slice(0 until chunkSize, chunkSize until 2 * chunkSize)
    chunked[1][0] = matrix.slice(chunkSize until 2 * chunkSize, 0 until chunkSize)
    chunked[1][1] = matrix.slice(chunkSize until 2 * chunkSize, chunkSize until 2 * chunkSize)
    return chunked
}

private fun fromChunked(chunked: ChunkedMatrix): Matrix =
    chunked[0][0].concatHorizontally(chunked[0][1])
        .concatVertically(chunked[1][0].concatHorizontally(chunked[1][1]))