package expressions

import org.junit.Assert
import org.junit.Test
import panel.Expression
import panel.Expression.Round
import panel.roundX
import panel.toValue
import java.math.BigDecimal

class TestRangeBrute {
    private fun check(expexted: String, exp: Expression, f: (BigDecimal) -> BigDecimal) {
        val d = "0.001".toBigDecimal()
        val gap = d * "30".toBigDecimal()
        val end = exp.range.end + gap
        val expAct = generateSequence(exp.range.start - gap) { it + d }
                .takeWhile { it <= end }
                .map { it to f(it).toPlainString() }
                .toList()
        expAct
                .forEach { (v, actualResult) ->
                    val contains = exp.contains(v)
                    if (contains) {
                        Assert.assertEquals("Double round exp $v", expexted, actualResult)
                    } else {
                        Assert.assertNotEquals("Double round uexp $v", expexted, actualResult)
                    }
                }

    }

    @Test
    fun zeroOnce() {
        check("0", Round("0".toValue()), { it.roundX(0) })
    }

    @Test
    fun someNumberOnce() {
        check("123.4", Round("123.4".toValue()), { it.roundX(1) })
    }

    @Test
    fun zeroDouble() {
        check("0", Round(Round("0".toValue())), { it.roundX(1).roundX(0) })
    }

    @Test
    fun someNumberDouble() {
        check("123.4", Round(Round("123.4".toValue())), { it.roundX(2).roundX(1) })
    }

    @Test
    fun roundNegate1() = check("-100.3", Round("-100.3".toValue()), { it.roundX(1) })

    @Test
    fun roundNegate2() = check("100.3", Round(Expression.Negate("100.3".toValue())), { it.negate().roundX(1) })

    @Test
    fun roundNegate3() = check("100.3", Expression.Negate(Round("100.3".toValue())), { it.roundX(1).negate() })


}