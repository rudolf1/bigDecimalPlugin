package ops//import liveplugin.PluginUtil.registerAction

import org.junit.Assert
import org.junit.Test
import panel.Expression
import panel.toValue
import factorize.unround


class ReverseTest {

    @Test
    fun opTest() {
        val v = Expression.Reverse("10".toValue().unround())
        Assert.assertEquals("0.09523809", v.range.start.toPlainString().take(10))
        Assert.assertEquals(false, v.range.startInclusive)
        Assert.assertEquals("0.10526315", v.range.end.toPlainString().take(10))
        Assert.assertEquals(true, v.range.endInclusive)
        Assert.assertEquals(2, v.ops)
    }
    @Test
    fun opTest1() {
        val v = Expression.Reverse("10".toValue())
        Assert.assertEquals(".1", v.range.start.toPlainString().trim('0'))
        Assert.assertEquals(true, v.range.startInclusive)
        Assert.assertEquals(".1", v.range.end.toPlainString().trim('0'))
        Assert.assertEquals(true, v.range.endInclusive)
        Assert.assertEquals(1, v.ops)
    }

}
