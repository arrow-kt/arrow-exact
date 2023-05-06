package arrow.exact

import arrow.core.Either
import arrow.core.raise.Raise
import arrow.core.raise.either


interface Exact<A, out R : Refined<A>> : ExactWithError<A, ExactError, R>

data class ExactError(val message: String)

interface ExactWithError<A, out E : Any, out R : Refined<A>> {

  fun Raise<E>.from(value: A): R

  fun fromOrNull(value: A): R? = either { from(value) }.getOrNull()

  fun fromOrThrow(value: A): R = when (val result = either { from(value) }) {
    is Either.Left -> throw ExactException(result.value)
    is Either.Right -> result.value
  }

  operator fun invoke(value: A): R = fromOrThrow(value)
}

class ExactException(error: Any) : IllegalArgumentException("ArrowExact error: $error")

interface Refined<A> {
  val value: A
}

