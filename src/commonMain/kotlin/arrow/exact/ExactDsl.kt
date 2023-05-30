package arrow.exact

import arrow.core.Either
import arrow.core.raise.Raise
import arrow.core.raise.RaiseDSL

@RaiseDSL
public inline fun <A, B, Error : Any> Raise<Error>.ensure(raw: A, exact: ExactEither<Error, A, B>): B {
  return when (val result = exact.from(raw)) {
    is Either.Left -> raise(result.value)
    is Either.Right -> result.value
  }
}

@RaiseDSL
public inline fun <A, B, Error> Raise<Error>.ensure(raw: A, exact: Exact<A, B>, error: (ExactError) -> Error): B {
  return when (val result = exact.from(raw)) {
    is Either.Left -> raise(error(result.value))
    is Either.Right -> result.value
  }
}
