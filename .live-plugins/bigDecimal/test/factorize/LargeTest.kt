package factorize

import org.junit.Test
import panel.Expression
import panel.toValue
import java.math.BigDecimal

fun primes(size: Int = 1000): List<BigDecimal> {
    val arr = Array(size) { true }
    return arr
            .mapIndexedNotNull { index: Int, b: Boolean ->
                when {
                    b -> {
                        for (j in index * 2 until size step index) {
                            arr[j] = false
                        }
                        index.toBigDecimal()
                    }
                    else -> null
                }
            }
}

class LargeTest {
    @Test
    fun test1() = notFound(
            search = "100",
            nums = sequence<String> { yield("2") }.take(3).toList().toTypedArray()
    )

    @Test
    fun test2() {
        val n = 15
        val two = "2".toValue()
        check(
                expected = Expression.Plus(generateSequence { two }.take(n).toList()),
                search = two.range.start.multiply(n.toBigDecimal()).toPlainString(),
                opsCount = n - 1,
                nums = sequence<String> { yield("2") }.take(n).toList().toTypedArray()
        )
    }

    @Test
    fun testPrimes() {
        val n = 3
        val primes = primes()
        val args = MutableList(n) { primes.random() }
        val target = args.sumOf { it }
        println("Searching $target")
        check(
                expected = Expression.Plus(args.map { it.toValue() }),
                search = target.toPlainString(),
                opsCount = 10,
                nums = primes.map { it.toPlainString() }.toTypedArray()
        )
    }
}
