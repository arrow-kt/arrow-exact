package arrow.exact

import arrow.core.Either
import arrow.core.raise.Raise
import kotlin.jvm.JvmInline

public typealias ExactValueSpec<T> = Exact<T, T>

@JvmInline
public value class ExactValue<T, Cond> private constructor(public val value: T) {
    public companion object {
        public fun <T, Cond : ExactValueSpec<T>> fromSpec(
            value: T,
            cond: Cond
        ): Either<ExactError, ExactValue<T, Cond>> =
            cond.from(value).map { ExactValue(it) }
    }
}

public class And<T>(private val a: Exact<T, T>, private val b: ExactValueSpec<T>) : ExactValueSpec<T> {
    override fun Raise<ExactError>.spec(raw: T): T {
        ensure(raw, a)
        ensure(raw, b)
        return raw
    }
}

public class NotBlank<T>(private val blankInstance: BlankInstance<T>) : ExactValueSpec<T> {
    override fun Raise<ExactError>.spec(raw: T): T =
        if (blankInstance.check(raw)) raw
        else raise(ExactError("Failed condition."))
}

public object Trimmed : ExactValueSpec<String> {
    override fun Raise<ExactError>.spec(raw: String): String {
        ensure(raw.trim() == raw)
        return raw
    }
}

public interface BlankInstance<T> {
    public fun check(t: T): Boolean

    public companion object {
        public val stringInstance: BlankInstance<String> =
            object : BlankInstance<String> {
                override fun check(t: String): Boolean = t.isNotBlank()
            }
    }
}
