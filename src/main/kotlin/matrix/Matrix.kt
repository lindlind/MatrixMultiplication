package matrix

import java.lang.IllegalArgumentException
import kotlin.math.max
import kotlin.math.min

class Matrix private constructor(
    private val matrix: Array<Array<Double>>,
    val rows: Int,
    val columns: Int
) {

    constructor(rows: Int, columns: Int) :
            this(Array(rows) { Array(columns) { 0.0 } }, rows, columns)

    constructor(rows: Int, columns: Int, value: () -> Double) :
            this(Array(rows) { Array(columns) { value() } }, rows, columns)

    companion object {

        fun from2dArray(array: Array<Array<Double>>): Matrix {
            val rowSizes = array.map { it.size }.distinct()
            if (rowSizes.size > 1) throw IllegalArgumentException("Different size of rows")
            if (array.isEmpty() || array[0].isEmpty()) return emptyMatrix()
            return Matrix(array, array.size, array[0].size)
        }

        fun from2dList(list: List<List<Double>>) =
            from2dArray(list.map { it.toTypedArray() }.toTypedArray())

        fun emptyMatrix(): Matrix = Matrix(emptyArray(), 0, 0)

    }

    fun isEmpty() = rows == 0 || columns == 0

    operator fun get(i: Int, j: Int) = when {
        (i >= rows || j >= columns) -> 0.0
        else -> matrix[i][j]
    }

    operator fun set(i: Int, j: Int, value: Double) {
        if (i < rows && j < columns) matrix[i][j] = value
    }

    fun map(f: (Double) -> Double): Matrix = from2dList(matrix.map { row -> row.map { x -> f(x) } })

    operator fun plus(other: Matrix): Matrix {
        val resultRows = max(this.rows, other.rows)
        val resultColumns = max(this.columns, other.columns)
        val result = Matrix(resultRows, resultColumns)
        for (i in 0 until resultRows) for (j in 0 until resultColumns) {
            result[i, j] += this[i, j]
            result[i, j] += other[i, j]
        }

        return result
    }

    operator fun minus(other: Matrix): Matrix {
        val resultRows = max(this.rows, other.rows)
        val resultColumns = max(this.columns, other.columns)
        val result = Matrix(resultRows, resultColumns)
        for (i in 0 until resultRows) for (j in 0 until resultColumns) {
            result[i, j] += this[i, j]
            result[i, j] -= other[i, j]
        }

        return result
    }

    operator fun times(k: Double): Matrix = map { x -> x * k }

    fun slice(rowsRange: IntRange, columnsRange: IntRange): Matrix {
        if (rowsRange.first >= rows || columnsRange.first >= columns) return emptyMatrix()
        if (rowsRange.isEmpty() || columnsRange.isEmpty()) return emptyMatrix()
        return slice(
            rowsRange.first, min(rows, rowsRange.last + 1),
            columnsRange.first, min(columns, columnsRange.last + 1)
        )
    }

    private fun slice(rowFrom: Int, rowUntil: Int, columnFrom: Int, columnUntil: Int): Matrix {
        val sliced = Matrix(rowUntil - rowFrom, columnUntil - columnFrom)
        for (i in rowFrom until rowUntil) for (j in columnFrom until columnUntil) {
            sliced[i - rowFrom, j - columnFrom] = this[i, j]
        }
        return sliced
    }

    fun transpose(): Matrix {
        val transposed = Matrix(this.columns, this.rows)
        for (i in 0 until rows) for (j in 0 until columns) {
            transposed[i, j] = this[j, i]
        }
        return transposed
    }

    fun print() {
        for (row in matrix) {
            for (x in row) {
                print("$x".padStart(7))
            }
            println()
        }
        println()
    }
}
