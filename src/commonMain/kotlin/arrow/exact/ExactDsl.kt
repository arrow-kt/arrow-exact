package arrow.exact

import arrow.core.Either
import arrow.core.raise.Raise
import arrow.core.raise.RaiseDSL

@RaiseDSL
public fun Raise<ErrorMessage>.ensure(condition: Boolean): Unit = ensure(condition) { "Failed condition." }

@RaiseDSL
public fun Raise<ErrorMessage>.ensure(condition: Boolean, lazyMessage: () -> String) {
  if (!condition) raise(ErrorMessage(lazyMessage()))
}

@RaiseDSL
public fun <A, B> Raise<ErrorMessage>.ensure(raw: A, exact: ExactEither<*, A, B>): B =
  ensure(raw, exact) { ErrorMessage("Failed to match Exact.") }

@RaiseDSL
public inline fun <A, B, Error : Any, E : Any> Raise<E>.ensure(raw: A, exact: ExactEither<Error, A, B>, error: (Error) -> E): B {
  return when (val result = exact.from(raw)) {
    is Either.Left -> raise(error(result.value))
    is Either.Right -> result.value
  }
}
