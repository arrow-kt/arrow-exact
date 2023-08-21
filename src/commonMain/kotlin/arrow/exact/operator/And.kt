package arrow.exact.operator

import arrow.core.raise.Raise
import arrow.exact.Exact
import arrow.exact.ExactError
import arrow.exact.ExactValueSpec
import arrow.exact.ensure

public class And<T>(private val a: Exact<T, T>, private val b: ExactValueSpec<T>) : ExactValueSpec<T> {
    override fun Raise<ExactError>.spec(raw: T): T {
        ensure(raw, a)
        ensure(raw, b)
        return raw
    }
}
