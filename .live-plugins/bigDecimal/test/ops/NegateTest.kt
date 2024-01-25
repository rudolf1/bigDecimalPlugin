package ops//import liveplugin.PluginUtil.registerAction

import org.junit.Assert
import org.junit.Test
import panel.Expression
import panel.toValue


class NegateTest {

    @Test
    fun opTest() {
        val v = Expression.Negate("100.33".toValue())
        Assert.assertFalse(v.contains("-100.32499999".toValue()))
        Assert.assertTrue(v.contains("-100.325".toValue()))
        Assert.assertTrue(v.contains("-100.334999999".toValue()))
        Assert.assertFalse(v.contains("-100.335".toBigDecimal()))
        Assert.assertFalse(v.contains("-100.336".toValue()))
        Assert.assertEquals(1, v.ops)
    }
    @Test
    fun op() {
        val range = "-100.33".toValue().range
        val range1 = Expression.Negate("100.33".toValue()).range
        Assert.assertEquals(range.start, range1.start)
        Assert.assertEquals(range.end, range1.end)
        Assert.assertEquals(range.startInclusive, range1.startInclusive)
        Assert.assertEquals(range.endInclusive, range1.endInclusive)
        Assert.assertEquals(range.value, range1.value!!.negate())
        Assert.assertEquals(range.ops, range1.ops)
    }

}
