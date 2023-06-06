package arrow.exact

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.raise.Raise
import arrow.core.raise.RaiseDSL

@RaiseDSL
public inline fun Raise<ExactError>.ensure(condition: Boolean) {
  if (!condition) raise(ExactError("Failed condition."))
}

@RaiseDSL
public inline fun <A, B> Raise<ExactError>.ensure(raw: A, exact: ExactEither<*, A, B>): B =
  ensure(raw, exact) { ExactError("Failed to match Exact.") }

@RaiseDSL
public inline fun <A, B, Error : Any, E : Any> Raise<E>.ensure(raw: A, exact: ExactEither<Error, A, B>, error: (Error) -> E): B =
  exact.from(raw) getOrElse { raise(error(it)) }
