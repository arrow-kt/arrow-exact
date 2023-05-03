package arrow.exact

import arrow.core.*

interface Exact<A, out B> {

  fun fromOrEither(value: A): Either<ExactError, B>

  fun fromOrNull(value: A): B? {
    return fromOrEither(value).orNull()
  }

  fun fromOrThrow(value: A): B {
    return when (val result = fromOrEither(value)) {
      is Either.Left -> throw IllegalArgumentException(result.value.message)
      is Either.Right -> result.value
    }
  }
}


