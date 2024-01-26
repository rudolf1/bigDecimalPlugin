package expressions

import org.junit.Assert
import org.junit.Test
import panel.Expression
import panel.toValue


class PlusTest {

    @Test
    fun x() {
        Assert.assertTrue(Expression.Plus("100".toValue(), "1".toValue()).contains("101".toValue()))
        Assert.assertTrue(Expression.Plus("100".toValue(), "1".toValue()).contains("101".toBigDecimal()))
    }

}
