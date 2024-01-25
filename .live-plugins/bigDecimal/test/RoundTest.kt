import org.junit.Assert
import org.junit.Test
import panel.roundX

class RoundTest {

    @Test
    fun name() {
        Assert.assertEquals("1.5", "1.49".toBigDecimal().roundX(1).toPlainString())
        Assert.assertEquals("1.15", "1.149".toBigDecimal().roundX(2).toPlainString())
        Assert.assertEquals("2", "2.4".toBigDecimal().roundX(0).toPlainString())
        Assert.assertEquals("3", "2.5".toBigDecimal().roundX(0).toPlainString())

        Assert.assertEquals("250", "254".toBigDecimal().roundX(-1).toPlainString())
        Assert.assertEquals("300", "254".toBigDecimal().roundX(-2).toPlainString())
        Assert.assertEquals("254", "254".toBigDecimal().roundX(2).toPlainString())
    }
}