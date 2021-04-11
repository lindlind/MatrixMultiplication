import matrix.Matrix
import matrix.times
import java.time.Duration
import java.time.Instant
import kotlin.random.Random

private const val MAX_SIDE_SIZE_TO_PRINT = 50

fun generateRandomMatrix(rows: Int, columns: Int) =
    Matrix(rows, columns).map { Random.nextDouble(-20.0, 20.0) }

fun testDot(m1: Matrix, m2: Matrix, printSpentTime: Boolean = true) {
    val startTime = Instant.now()
    val result = m1 * m2
    val endTime = Instant.now()

    m1.prettyPrint("A")
    m2.prettyPrint("B")
    result.prettyPrint("A*B")

    val spentTimeMillis = Duration.between(startTime, endTime).toMillis()
    if (printSpentTime && spentTimeMillis > 1) {
        println("Spent time: ${spentTimeMillis.toDouble()/1000} seconds")
        println()
    }
}

fun sampleSingleton() {
    val m1 = Matrix(1, 1) { 3.0 }
    val m2 = Matrix(1, 1) { 4.0 }
    testDot(m1, m2)
}

fun sampleSquaredSmall() {
    val m1 = Matrix(4, 4) { 3.0 }
    val m2 = Matrix(4, 4) { 4.0 }
    testDot(m1, m2)
}

fun sampleSquaredLarge() {
    val m1 = Matrix(512, 512) { 3.0 }
    val m2 = Matrix(512, 512) { 4.0 }
    testDot(m1, m2)
}

fun sampleRectangle() {
    val m1 = Matrix(3, 4) { 3.0 }
    val m2 = Matrix(4, 5) { 4.0 }
    testDot(m1, m2)
}

fun sampleThinToSingleton() {
    val m1 = Matrix(1, 512) { 3.0 }
    val m2 = Matrix(512, 1) { 4.0 }
    testDot(m1, m2)
}

fun sampleThinToSquared() {
    val m1 = Matrix(512, 1) { 3.0 }
    val m2 = Matrix(1, 512) { 4.0 }
    testDot(m1, m2)
}

fun sampleCreatedByHand() {
    val m1 = Matrix.from2dList(listOf(
        listOf(-1, 2, 3, 4, -5).map { it.toDouble() },
        listOf(2, 3, -4, 7, 8).map { it.toDouble() },
        listOf(5, 0, 9, -2, 7).map { it.toDouble() }
    ))
    val m2 = Matrix.from2dList(listOf(
        listOf(1, 2, -3, 1).map { it.toDouble() },
        listOf(2, 3, 4, 0).map { it.toDouble() },
        listOf(5, -8, 3, 9).map { it.toDouble() },
        listOf(-1, -2, -3, 6).map { it.toDouble() },
        listOf(9, 7, -4, -7).map { it.toDouble() }
    ))
    testDot(m1, m2)

    val expected = Matrix.from2dList(listOf(
        listOf(-31, -63, 28, 85).map { it.toDouble() },
        listOf(53, 87, -59, -48).map { it.toDouble() },
        listOf(115, -9, -10, 25).map { it.toDouble() }
    ))
    expected.prettyPrint("A*B expected")
}

fun sampleRandomSmall() {
    val a = Random.nextInt(5, 50)
    val b = Random.nextInt(5, 50)
    val c = Random.nextInt(5, 50)
    val m1 = generateRandomMatrix(a, b)
    val m2 = generateRandomMatrix(b, c)
    testDot(m1, m2)
}

fun sampleRandomLarge() {
    val a = Random.nextInt(500, 1500)
    val b = Random.nextInt(500, 1500)
    val c = Random.nextInt(500, 1500)
    val m1 = generateRandomMatrix(a, b)
    val m2 = generateRandomMatrix(b, c)
    testDot(m1, m2)
}

private fun Matrix.prettyPrint(matrixName: String) {
    println("$matrixName:")
    if (this.rows <= MAX_SIDE_SIZE_TO_PRINT && this.columns <= MAX_SIDE_SIZE_TO_PRINT) {
        this.print()
    }
    println("shape of $matrixName: ${this.shape()}")
    println()
}

private fun Matrix.shape() = "(${this.rows}, ${this.columns})"
