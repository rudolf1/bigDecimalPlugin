package ops

import org.junit.Assert
import org.junit.Test
import panel.Expression
import panel.toValue
import factorize.unround

class PlusTest {
    @Test
    fun testPositivePositive() {
        val v = Expression.Plus("100".toValue().unround(), "101".toValue().unround())
        Assert.assertEquals("200.0".toBigDecimal(), v.range.start)
        Assert.assertEquals(true, v.range.startInclusive)
        Assert.assertEquals("202.0".toBigDecimal(), v.range.end)
        Assert.assertEquals(false, v.range.endInclusive)
        Assert.assertEquals(3, v.ops)
    }
    @Test
    fun testPositiveNegative() {
        val a = "100".toValue().unround()
        val b = "-5".toValue().unround()
        val v = Expression.Plus(a, b)
        Assert.assertEquals("94.0".toBigDecimal(), v.range.start)
        Assert.assertEquals(false, v.range.startInclusive)
        Assert.assertEquals("96.0".toBigDecimal(), v.range.end)
        Assert.assertEquals(false, v.range.endInclusive)
        Assert.assertEquals(3, v.ops)
    }
    @Test
    fun testNegativePositive() {
        val a = "-100".toValue().unround()
        val b = "5".toValue().unround()
        val v = Expression.Plus(a, b)
        Assert.assertEquals("-96.0".toBigDecimal(), v.range.start)
        Assert.assertEquals(false, v.range.startInclusive)
        Assert.assertEquals("-94.0".toBigDecimal(), v.range.end)
        Assert.assertEquals(false, v.range.endInclusive)
        Assert.assertEquals(3, v.ops)
    }
    @Test
    fun testNegativeNegative() {
        val a = "-100".toValue().unround()
        val b = "-101".toValue().unround()
        val v = Expression.Plus(a, b)
        Assert.assertEquals("-202.0".toBigDecimal(), v.range.start)
        Assert.assertEquals(false, v.range.startInclusive)
        Assert.assertEquals("-200.0".toBigDecimal(), v.range.end)
        Assert.assertEquals(true, v.range.endInclusive)
        Assert.assertEquals(3, v.ops)
    }

}