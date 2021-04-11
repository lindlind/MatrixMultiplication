import matrix.*

fun main(args: Array<String>) {

    val m1 = Matrix.from2dArray(arrayOf(
        arrayOf(1.0, 2.0, -3.0),
        arrayOf(0.1, 0.02, 0.003)
    ))
    val m2 = Matrix.from2dArray(arrayOf(
        arrayOf(100.0, 1.0),
        arrayOf(1.0, 1.0),
        arrayOf(0.01, 1.0)
    ))

    // Try one of prepared samples
    // more samples in Samples.kt, function starts with 'sample'
    sampleRandomSmall()

    println("------------------------------------------------------------")

    // Try your custom sample with testDot function
    testDot(m1, m2)

    println("------------------------------------------------------------")

    // Try multiplication by yourself
    val result = m1 * m2
    result.print()

}
