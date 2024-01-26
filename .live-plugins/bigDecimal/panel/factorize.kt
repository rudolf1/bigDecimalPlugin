package panel

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.absoluteValue

fun BigDecimal.roundX(x: Int): BigDecimal {
    return if (x > 0) {
        this
                .multiply(BigDecimal.TEN.pow(x))
                .setScale(0, RoundingMode.HALF_UP)
                .divide(BigDecimal.TEN.pow(x))
    } else {
        this
                .divide(BigDecimal.TEN.pow(x.absoluteValue))
                .setScale(0, RoundingMode.HALF_UP)
                .multiply(BigDecimal.TEN.pow(x.absoluteValue))
    }
}

fun factorize(requiredValue: List<BigDecimal>, otherValues: List<BigDecimal>, limitOps: Int = 3): List<Expression> {
    var values = otherValues.map { it.toValue() as Expression }
            .toSet()
    val requiredRanges = requiredValue.map { Expression.Round(it.toValue()) }

    repeat(limitOps) {
        val src = values.toList()
        val one = src.flatMap { num ->
            Expression.one.map { op ->
                op(num)
            }
        }
        val two = src.flatMap { num1 ->
            src.flatMap { num2 ->
                Expression.two.map { op ->
                    op(num1, num2)
                }
            }
        }
        values = (one + two)
                .mapNotNull {
                    runCatching {
                        it
                    }
                            .onFailure {
                                it.printStackTrace()
                            }
                            .getOrNull()

                }.toSet() + values
        values = values.distinctBy { it.range to it.ops }.toSet()
        println("Size after ${it + 1} operations: ${values.size}")

        val answer = values
                .filter { requiredRanges.all { req -> it.contains(req) } }
                .sortedBy { it.ops }
        if (answer.isNotEmpty()) {
            return answer.sortedBy { it.ops }
        }
    }
    return emptyList()
}