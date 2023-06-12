package arrow.exact

import arrow.core.Either
import arrow.core.raise.Raise
import arrow.core.raise.RaiseDSL

@RaiseDSL
public fun Raise<String>.ensure(condition: Boolean) {
  if (!condition) raise("Failed condition.")
}

@RaiseDSL
public inline fun <A, B> Raise<String>.ensure(raw: A, exact: ExactEither<*, A, B>): B =
  ensure(raw, exact) { "Failed to match Exact." }

@RaiseDSL
public inline fun <A, B, Error : Any, E : Any> Raise<E>.ensure(raw: A, exact: ExactEither<Error, A, B>, error: (Error) -> E): B {
  return when (val result = exact.from(raw)) {
    is Either.Left -> raise(error(result.value))
    is Either.Right -> result.value
  }
}
