package ops

import org.junit.Assert
import org.junit.Test
import panel.Expression.Round
import panel.roundX
import panel.toValue

class RoundingTest {
    @Test
    fun positive1() {
        val v = Round("100".toValue())
        Assert.assertFalse(v.contains("99.499999999".toBigDecimal()))
        Assert.assertTrue(v.contains("99.5".toBigDecimal()))
        Assert.assertTrue(v.contains("99.51111".toBigDecimal()))
        Assert.assertTrue(v.contains("100.4".toBigDecimal()))
        Assert.assertTrue(v.contains("100.49999999".toBigDecimal()))
        Assert.assertFalse(v.contains("100.5".toBigDecimal()))
    }

    @Test
    fun positive2() {
        val v = Round("100.33".toValue())
        Assert.assertFalse(v.contains("100.32499999".toBigDecimal()))
        Assert.assertTrue(v.contains("100.325".toBigDecimal()))
        Assert.assertTrue(v.contains("100.334999999".toBigDecimal()))
        Assert.assertFalse(v.contains("100.335".toBigDecimal()))
    }

    @Test
    fun negative2() {
        val v = Round("-100.33".toValue())
        Assert.assertFalse(v.contains("-100.32499999".toBigDecimal()))
        Assert.assertTrue(v.contains("-100.325".toBigDecimal()))
        Assert.assertTrue(v.contains("-100.334999999".toBigDecimal()))
        Assert.assertFalse(v.contains("-100.335".toBigDecimal()))
    }

    @Test
    fun zero() {
        val v = Round("0".toValue())
        Assert.assertFalse(v.contains("-0.5".toBigDecimal()))
        Assert.assertTrue(v.contains("-0.499".toBigDecimal()))
        Assert.assertTrue(v.contains("-0.49".toBigDecimal()))
        Assert.assertTrue(v.contains("0.49".toBigDecimal()))
        Assert.assertFalse(v.contains("0.5".toBigDecimal()))
    }


    @Test
    fun zero1() {
        val v = Round("0.1".toValue())
        Assert.assertFalse(v.contains("0.049".toBigDecimal()))
        Assert.assertTrue(v.contains("0.05".toBigDecimal()))
        Assert.assertTrue(v.contains("0.149".toBigDecimal()))
        Assert.assertFalse(v.contains("0.15".toBigDecimal()))
    }
}