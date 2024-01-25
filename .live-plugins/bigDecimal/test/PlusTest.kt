//import liveplugin.PluginUtil.registerAction

import org.junit.Test
import panel.Expression
import panel.toValue


class PlusTest {

    @Test
    fun single() = check(
            expected = Expression.Plus("100".toValue(), "1".toValue()),
                    search = "101",
                    "100", "1"
            )

}
