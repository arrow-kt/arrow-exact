package arrow.exact

import arrow.core.Either
import kotlin.jvm.JvmInline

public typealias ExactValueSpec<T> = Exact<T, T>

@JvmInline
public value class ExactValue<T, Cond> private constructor(private val value: T) {
    public companion object {
        public fun <T, Cond : ExactValueSpec<T>> fromSpec(
            value: T,
            cond: Cond
        ): Either<ExactError, ExactValue<T, Cond>> =
            cond.from(value).map { ExactValue(it) }
    }
}
