import org.junit.Test
import panel.Expression
import panel.toValue

class LargeTest {
    @Test
    fun test1() = notFound(
            search = "100",
            nums = sequence<String> { yield("2") }.take(3).toList().toTypedArray()
    )

    @Test
    fun test2() {
        val two = "2".toValue()

        check(
                expected = Expression.Plus(generateSequence { two }.take(6).toList()),
                search = "12",
                opsCount = 5,
                nums = sequence<String> { yield("2") }.take(6).toList().toTypedArray()
        )
    }
}
