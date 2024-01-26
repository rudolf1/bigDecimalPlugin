package ops//import liveplugin.PluginUtil.registerAction

import org.junit.Assert
import org.junit.Test
import panel.Expression.Negate
import panel.toValue
import factorize.unround


class NegateTest {

    @Test
    fun negateUnround() {
        val v = Negate("100.33".toValue().unround())
        Assert.assertFalse(v.contains("-100.32499999".toValue()))
        Assert.assertTrue(v.contains("-100.325".toValue()))
        Assert.assertTrue(v.contains("-100.334999999".toValue()))
        Assert.assertFalse(v.contains("-100.335".toBigDecimal()))
        Assert.assertFalse(v.contains("-100.336".toValue()))
        Assert.assertEquals(2, v.ops)
    }

}
