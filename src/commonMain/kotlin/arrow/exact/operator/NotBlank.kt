package arrow.exact.operator

import arrow.core.raise.Raise
import arrow.exact.ExactError
import arrow.exact.ExactValueSpec

public class NotBlank<T>(private val blankInstance: BlankInstance<T>) : ExactValueSpec<T> {
    override fun Raise<ExactError>.spec(raw: T): T =
        if (blankInstance.check(raw)) raw
        else raise(ExactError("\"$raw\" contains blank."))
}

public object Trimmed : ExactValueSpec<String> {
    override fun Raise<ExactError>.spec(raw: String): String =
        if (raw.trim() == raw) raw
        else raise(ExactError("\"$raw\" contains whitespace."))

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
