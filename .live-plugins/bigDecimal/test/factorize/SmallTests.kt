package factorize

import org.junit.Test
import panel.Expression
import panel.toValue

class SmallTests {
    @Test
    fun singlePlus() = check(
            expected = Expression.Plus("100".toValue(), "1".toValue()),
            search = "101",
            "100", "1"
    )
}