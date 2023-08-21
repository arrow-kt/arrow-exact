package arrow.exact.operator

import arrow.core.raise.Raise
import arrow.exact.ExactError
import arrow.exact.ExactValueSpec
import arrow.exact.ensure

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
