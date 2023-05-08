package arrow.exact

import arrow.core.Either


interface Exact<A, out R> : ExactEither<ExactError, A, R>

data class ExactError(val message: String)

interface ExactEither<out E : Any, A, out R> {

  fun from(value: A): Either<E, R>

  // TODO: This doesn't work for some weird reason :/
  // fun Raise<E>.fromOrRaise(value: A): R = from(value).bind()

  fun fromOrNull(value: A): R? = from(value).getOrNull()

  fun fromOrThrow(value: A): R = when (val result = from(value)) {
    is Either.Left -> throw ExactException(result.value)
    is Either.Right -> result.value
  }

  // TODO: What are your thoughts about this?
  operator fun invoke(value: A): R = fromOrThrow(value)
}

class ExactException(error: Any) : IllegalArgumentException("ArrowExact error: $error")
