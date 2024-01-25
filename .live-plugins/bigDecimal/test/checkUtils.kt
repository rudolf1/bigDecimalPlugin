import org.junit.Assert
import panel.Expression
import panel.factorize
import panel.toValue

fun check(
        expected: Expression,
        search: String,
        vararg nums: String,
        opsCount: Int = 3
) {
    val actual = factorize(
            listOf(search.toBigDecimal()),
            nums.map { it.toBigDecimal() },
            limitOps = opsCount,
    ).toSet()

    if (!actual.contains(expected)) {
        println("${search.toValue().range}, expected ${expected.range}(ops ${expected.ops}): ${expected}")
        actual.forEach {
            println("Actual: ${it.range}(ops ${it.ops}): $it")
        }
        Assert.fail("Not found")
    }
}

fun notFound(search: String, vararg nums: String) {
    val result = factorize(
            listOf(search.toBigDecimal()),
            nums.map { it.toBigDecimal() },
    )
    if (result.isNotEmpty()) {
        result.forEach {
            println("${search.toValue().range} intersects ${it.range}(ops ${it.ops}): $it")
        }
        Assert.fail()
    }
}
