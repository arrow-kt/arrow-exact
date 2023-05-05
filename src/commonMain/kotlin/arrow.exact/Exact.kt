package arrow.exact

import arrow.core.Either

interface Exact<A, out B> {

  fun from(value: A): Either<ExactError, B>

  fun fromOrNull(value: A): B? {
    return from(value).getOrNull()
  }

  fun fromOrThrow(value: A): B {
    return when (val result = from(value)) {
      is Either.Left -> throw ExactException(result.value.message)
      is Either.Right -> result.value
    }
  }
}

class ExactError(val message: String)

class ExactException(message: String) : IllegalArgumentException(message)
