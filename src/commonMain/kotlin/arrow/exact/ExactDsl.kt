package arrow.exact

import arrow.core.Either
import arrow.core.raise.Raise
import arrow.core.raise.RaiseDSL

@RaiseDSL
public inline fun Raise<ExactError>.ensure(condition: Boolean) {
  if (!condition) raise(ExactError("Failed condition."))
}

@RaiseDSL
public inline fun <A, B> Raise<ExactError>.ensureExact(raw: A, exact: Exact<A, B>): B = ensureExact(raw, exact as ExactEither<*, A, B>)

@RaiseDSL
public inline fun <A, B> Raise<ExactError>.ensureExact(raw: A, exact: ExactEither<*, A, B>): B = ensureExact(raw, exact) { ExactError("Failed to match Exact.") }

@RaiseDSL
public inline fun <A, B, Error : Any> Raise<Error>.ensureExact(raw: A, exact: Exact<A, B>, error: (ExactError) -> Error): B = ensureExact(raw, exact as ExactEither<ExactError, A, B>, error)

@RaiseDSL
public inline fun <A, B, Error : Any, E : Any> Raise<E>.ensureExact(raw: A, exact: ExactEither<Error, A, B>, error: (Error) -> E): B {
  return when (val result = exact.from(raw)) {
    is Either.Left -> raise(error(result.value))
    is Either.Right -> result.value
  }
}
