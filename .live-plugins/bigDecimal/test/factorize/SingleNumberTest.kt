package factorize

import org.junit.Test
import panel.Expression.Negate
import panel.toValue


class SingleNumberTest {

    @Test
    fun single() = check(
            expected = "100".toValue(),
            search = "100",
            "100"
    )

    @Test
    fun sourceRoundedTo0_1() = check(
            expected = "100.499999".toValue(),
            search = "100",
            "100.499999"
    )

    @Test
    fun sourceRoundedTo0_2() = check(
            expected = "99.5".toValue(),
            search = "100",
            "99.5"
    )

    @Test
    fun sourceRoundedTo0_3() = notFound(
            search = "100",
            "99.4"
    )
    // round(99.5) == 100
    // 99.5 = round(99.49)

    @Test
    fun sourceRoundedTo0_4() = check(
            expected = "-100.5".toValue().unround(),
            search = "-100",
            "-100.5"
    )

    @Test
    fun negateTest() = check(
            expected = Negate("-100".toValue()),
            search = "100",
            "-100"
    )


}
