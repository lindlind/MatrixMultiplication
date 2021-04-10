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
            val rowSizes = array.map{ it.size }.distinct()
            if (rowSizes.size > 1) throw IllegalArgumentException("Different size of rows")
            if (array.isEmpty() || array[0].isEmpty()) return emptyMatrix()
            return Matrix(array, array.size, array[0].size)
        }

        fun from2dList(list: List<List<Double>>) =
            from2dArray(list.map{ it.toTypedArray() }.toTypedArray())

        fun emptyMatrix(): Matrix = Matrix(emptyArray(), 0, 0)

    }

    fun isEmpty() = rows == 0 || columns == 0

    operator fun get(i: Int, j: Int) = when {
        (i >= rows || j >= columns) -> 0.0
        else -> matrix[i][j]
    }

    operator fun set(i: Int, j: Int, value: Double) {
        matrix[i][j] = value
    }

    fun map(f: (Double) -> Double): Matrix = from2dList(matrix.map { row -> row.map { x -> f(x) } })

    operator fun plus(other: Matrix): Matrix {
        val resultRows = max(this.rows, other.rows)
        val resultColumns = max(this.columns, other.columns)
        val result = Array(resultRows) { Array(resultColumns) { 0.0 } }
        for (i in 0 until resultRows) {
            for (j in 0 until resultColumns) {
                result[i][j] += this[i, j]
                result[i][j] += other[i, j]
            }
        }

        return Matrix(result, resultRows, resultColumns)
    }

    operator fun minus(other: Matrix): Matrix {
        val resultRows = max(this.rows, other.rows)
        val resultColumns = max(this.columns, other.columns)
        val result = Array(resultRows) { Array(resultColumns) { 0.0 } }
        for (i in 0 until resultRows) {
            for (j in 0 until resultColumns) {
                result[i][j] += this[i, j]
                result[i][j] -= other[i, j]
            }
        }

        return Matrix(result, resultRows, resultColumns)
    }

    operator fun times(k: Double): Matrix = map{ x -> x * k }

    fun slice(rowsRange: IntRange, columnsRange: IntRange): Matrix {
        if (rowsRange.start >= rows || columnsRange.start >= columns) return emptyMatrix()
        if (rowsRange.isEmpty() || columnsRange.isEmpty()) return emptyMatrix()

        val actualRows = rowsRange.start..min(rows - 1, rowsRange.endInclusive)
        val actualColumns = columnsRange.start..min(columns - 1, columnsRange.endInclusive)
        return from2dList(matrix.slice(actualRows).map { row -> row.slice(actualColumns) })
    }

    fun concatHorizontally(other: Matrix): Matrix {
        if (this.isEmpty()) return other
        if (other.isEmpty()) return this

        val actualRows = min(this.rows, other.rows)
        val matrixA = this.matrix.slice(0 until actualRows)
        val matrixB = other.matrix.slice(0 until actualRows)
        return from2dList(
            matrixA.zip(matrixB) { rowLeft, rowRight -> rowLeft.plus(rowRight).toList() }
        )
    }

    fun concatVertically(other: Matrix): Matrix {
        if (this.isEmpty()) return other
        if (other.isEmpty()) return this

        val actualColumns = min(this.columns, other.columns)
        val matrixA = this.matrix.map { it.slice(0 until actualColumns) }
        val matrixB = other.matrix.map { it.slice(0 until actualColumns) }
        return from2dList(matrixA.plus(matrixB))
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
