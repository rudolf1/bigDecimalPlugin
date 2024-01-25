package panel

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

fun BigDecimal.roundX(x: Int) = this.round(MathContext(this.precision() - this.scale() + x, RoundingMode.HALF_UP))

fun factorize(requiredValue: List<BigDecimal>, otherValues: List<BigDecimal>, limitOps: Int = 3): List<Expression> {
    var values = otherValues.map { it.toValue() as Expression }
            .associate { it.range to it }
    repeat(limitOps) {
        val src = values.toList()
        val one = src.flatMap { num ->
            Expression.one.map { op ->
                runCatching {
                    op(num.second)
                }
                        .onFailure {
                            it.printStackTrace()
                        }
                        .getOrNull()
            }
        }
        val two = src.flatMap { num1 ->
            src.flatMap { num2 ->
                Expression.two.map { op ->
                    runCatching {
                        op(num1.second, num2.second)
                    }
                            .onFailure {
                                it.printStackTrace()
                            }
                            .getOrNull()
                }
            }
        }
        values = (one + two).filterNotNull().associate { it.range to it }// + values
        println("Size after ${it + 1} operations: ${values.size}")
        val requiredRanges = requiredValue.map { it.toValue() }
        val answer = values.values
                .filter { requiredRanges.all { req -> it.contains(req) } }
                .sortedBy { it.ops }
        if (answer.isNotEmpty()) {
            return answer.sortedBy { it.ops }
        }
    }
    return emptyList()
}