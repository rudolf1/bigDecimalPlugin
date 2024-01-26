package panel

import java.math.BigDecimal
import java.math.RoundingMode

fun DecimalRange.normalize(): DecimalRange {
    return if (this.start > this.end) {
        DecimalRange(end, endInclusive, start, startInclusive, value = this.value)
    } else {
        this
    }
}

fun BigDecimal.toValue(): Expression {
    return Expression.DecimalValue(this)
}

fun String.toValue(): Expression = BigDecimal(this).toValue()


data class DecimalRange(val start: BigDecimal, val startInclusive: Boolean,
                        val end: BigDecimal, val endInclusive: Boolean, val value: BigDecimal? = null
) {
    override fun toString(): String {
        return listOfNotNull(
                value?.toPlainString(),
                if (start != end) {
                    listOf(
                            if (startInclusive) "[" else "(",
                            start.roundX(10),
                            "..",
                            end.roundX(10),
                            if (endInclusive) "]" else ")"
                    ).joinToString("", prefix = "{", postfix = "}")
                } else {
                    null
                }
        ).joinToString("")
    }
}

sealed class Expression {
    abstract val range: DecimalRange
    abstract val ops: Int
    fun contains(other: Expression): Boolean {
        val r1 = this.range
        val r2 = other.range
        return when {
            r1.endInclusive && r2.startInclusive && r1.end < r2.start -> false
            !(r1.endInclusive && r2.startInclusive) && r1.end <= r2.start -> false
            r2.endInclusive && r1.startInclusive && r2.end < r1.start -> false
            !(r2.endInclusive && r1.startInclusive) && r2.end < r1.start -> false
            else -> true
        }
    }

    fun contains(other: BigDecimal): Boolean {
        val r1 = this.range
        return when {
            r1.startInclusive && other < r1.start -> false
            !r1.startInclusive && other <= r1.start -> false
            r1.endInclusive && other > r1.end -> false
            !r1.endInclusive && other >= r1.end -> false
            else -> true
        }
    }

    data class DecimalValue(val value: BigDecimal) : Expression() {
        override val range: DecimalRange
            get() = DecimalRange(start = value,
                    startInclusive = true,
                    end = value,
                    endInclusive = true
            )
        override val ops: Int
            get() = 0

        override fun toString(): String {
            return value.roundX(10).toPlainString()
        }
    }

    data class Round(val value: Expression) : Expression() {
        private fun BigDecimal.toRoundRange(): DecimalRange {
            if (this == BigDecimal.ZERO) {
                return DecimalRange("-0.5".toBigDecimal(), false, "0.5".toBigDecimal(), false).normalize()
            }
            val start = if (this.scale() != 0) {
                BigDecimal(this.toPlainString() + "5")
            } else {
                BigDecimal(this.toPlainString() + ".5")
            }
            val end = this - (start - this)
            return DecimalRange(start, false, end, true).normalize()
        }

        override val range: DecimalRange
            get() {
                if (value !is DecimalValue) {
                    val r = value.range
                    val r1 = r.start.toRoundRange()
                    val r2 = r.end.toRoundRange()
                    return DecimalRange(
                            if (r.startInclusive) r1.start else r1.end,
                            r.startInclusive,
                            if (r.endInclusive) r2.end else r2.start,
                            r2.endInclusive
                    )
                }
                return value.value.toRoundRange()
            }
        override val ops: Int
            get() = value.ops + 1

        override fun toString(): String {

            val r = range
            return listOfNotNull(
                    "Round(",
                    value.toString(),
                    "{",
                    if (r.startInclusive) "[" else "(",
                    r.start.roundX(10),
                    "..",
                    r.end.roundX(10),
                    if (r.endInclusive) "]" else ")",
                    "})",
            ).joinToString("")
        }
    }

    data class Negate(val s: Expression) : Expression() {
        override val ops: Int
            get() = s.ops + 1

        override val range: DecimalRange
            get() = s.range.copy(
                    start = s.range.start.negate(),
                    end = s.range.end.negate()
            ).normalize()

        override fun toString(): String {
            return "(-$s)"
        }
    }

    data class Reverse(val s: Expression) : Expression() {
        override val ops: Int
            get() = s.ops + 1

        override val range: DecimalRange
            get() = s.range.copy(
                    start = BigDecimal.ONE.divide(s.range.start, 1000, RoundingMode.HALF_UP),
                    end = BigDecimal.ONE.divide(s.range.end, 1000, RoundingMode.HALF_UP)
            ).normalize()

        override fun toString(): String {
            return "(1/$s)"
        }
    }

    data class Plus(val a: List<Expression>) : Expression() {
        override val ops: Int
            get() = a.map { it.ops }.sum() + a.size - 1

        constructor(a: Expression, b: Expression) : this(
                listOf(a, b)
                        .flatMap {
                            if (it is Plus) {
                                it.a
                            } else {
                                listOf(it)
                            }
                        }
                        .sortedWith(compareBy(
                                { it.range.start },
                                { it.range.startInclusive },
                                { it.range.end },
                                { it.range.endInclusive },
                                { it.ops },
                        )))

        override val range: DecimalRange
            get() {
                val ranges = a.map { it.range }
                return DecimalRange(
                        ranges.sumOf { it.start },
                        ranges.map { it.startInclusive }.reduce({ a, b -> a && b }),
                        ranges.sumOf { it.end },
                        ranges.map { it.endInclusive }.reduce({ a, b -> a && b }),
                ).normalize()
            }

        override fun toString(): String {
            return "(${a.joinToString("+") { it.toString() }})"
        }
    }

    data class Mult(val a: List<Expression>) : Expression() {
        override val ops: Int
            get() = a.map { it.ops }.sum() + a.size - 1

        override val range: DecimalRange
            get() {

                val x = listOf(
                        a.first().range.start * a.last().range.start to (a.first().range.startInclusive && a.last().range.startInclusive),
                        a.first().range.start * a.last().range.end to (a.first().range.startInclusive && a.last().range.endInclusive),
                        a.first().range.end * a.last().range.start to (a.first().range.endInclusive && a.last().range.startInclusive),
                        a.first().range.end * a.last().range.end to (a.first().range.endInclusive && a.last().range.endInclusive),
                ).sortedBy { it.first }

                return DecimalRange(
                        x.first().first,
                        x.first().second,
                        x.last().first,
                        x.last().second
                )
            }

        constructor(a: Expression, b: Expression) : this(listOf(a, b).sortedWith(compareBy(
                { it.range.start },
                { it.range.startInclusive },
                { it.range.end },
                { it.range.endInclusive },
                { it.ops },
        )))
    }

    companion object {
        val one = listOf<(Expression) -> Expression>(
                { Negate(it) },
//                { Round(it) },
//                { Reverse(it) }
        )
        val two = listOf<(Expression, Expression) -> Expression>(
                { a, b -> Plus(a, b) },
//                { a, b -> Minus(a, b) },
//                { a, b -> Mult(a, b) },
//                { a, b -> Divide(a, b) },
        )
    }
}
