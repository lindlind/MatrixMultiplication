import matrix.*
import java.time.Duration
import java.time.Instant
import kotlin.math.pow

val pows = (1..10).map{ 2.0.pow(it) }
val sideSizes = listOf(8, 600, 800, 1000, 1200)//pows.flatMap { x -> pows.map { y -> (x + y).toInt() } }.filter { x -> x in 50..1024 }.distinct().sorted()

fun mul(m1: Matrix, m2: Matrix): Long {
    val startTime = Instant.now()
    val r = m1 * m2
    val endTime = Instant.now()
    return Duration.between(startTime, endTime).toMillis()
}

fun simpleMul(m1: Matrix, m2: Matrix): Long {
    val startTime = Instant.now()
    val r = dotSimple(m1, m2)
    val endTime = Instant.now()
    return Duration.between(startTime, endTime).toMillis()
}

fun main(args: Array<String>) {
    val stats = mutableListOf<List<Long>>()
    Matrix(1,1) * Matrix(1,1)
    for (a in sideSizes) for (b in sideSizes) for (c in sideSizes) {
        if (a != 8 && b != 8 && c != 8) continue
        val m1 = Matrix(a, b) {2.0}
        val m2 = Matrix(b, c) {2.0}

        val period = mul(m1, m2)
        val simplePeriod = simpleMul(m1, m2)
        stats.add(listOf(a.toLong(), b.toLong(), c.toLong(), period, simplePeriod))
        println("$a".padEnd(6) + "$b".padEnd(6) + "$c".padEnd(6) + "$period".padEnd(8) + "$simplePeriod".padEnd(8))
    }

}