package ops

import org.junit.Assert
import org.junit.Test
import panel.Expression
import panel.toValue

class PlusTest {
    @Test
    fun testPositivePositive() {
        val v = Expression.Plus("100".toValue(), "101".toValue())
        Assert.assertEquals("200.0".toBigDecimal(), v.range.start)
        Assert.assertEquals(true, v.range.startInclusive)
        Assert.assertEquals("202.0".toBigDecimal(), v.range.end)
        Assert.assertEquals(false, v.range.endInclusive)
        Assert.assertEquals(1, v.ops)
    }
    @Test
    fun testPositiveNegative() {
        val a = "100".toValue()
        val b = "-5".toValue()
        val v = Expression.Plus(a, b)
        Assert.assertEquals("94.0".toBigDecimal(), v.range.start)
        Assert.assertEquals(false, v.range.startInclusive)
        Assert.assertEquals("96.0".toBigDecimal(), v.range.end)
        Assert.assertEquals(false, v.range.endInclusive)
        Assert.assertEquals(1, v.ops)
    }
    @Test
    fun testNegativePositive() {
        val a = "-100".toValue()
        val b = "5".toValue()
        val v = Expression.Plus(a, b)
        Assert.assertEquals("-96.0".toBigDecimal(), v.range.start)
        Assert.assertEquals(false, v.range.startInclusive)
        Assert.assertEquals("-94.0".toBigDecimal(), v.range.end)
        Assert.assertEquals(false, v.range.endInclusive)
        Assert.assertEquals(1, v.ops)
    }
    @Test
    fun testNegativeNegative() {
        val a = "-100".toValue()
        val b = "-101".toValue()
        val v = Expression.Plus(a, b)
        Assert.assertEquals("-202.0".toBigDecimal(), v.range.start)
        Assert.assertEquals(false, v.range.startInclusive)
        Assert.assertEquals("-200.0".toBigDecimal(), v.range.end)
        Assert.assertEquals(true, v.range.endInclusive)
        Assert.assertEquals(1, v.ops)
    }

}