package arrow.exact

import arrow.core.Either
import arrow.core.raise.Raise


interface Exact<A, out R : Refined<A>> : ExactEither<ExactError, A, R>

data class ExactError(val message: String)

interface ExactEither<out E : Any, A, out R : Refined<A>> {

  fun from(value: A): Either<E, R>

  fun Raise<E>.fromOrRaise(value: A): R = from(value).bind()

  fun fromOrNull(value: A): R? = from(value).getOrNull()

  fun fromOrThrow(value: A): R = when (val result = from(value)) {
    is Either.Left -> throw ExactException(result.value)
    is Either.Right -> result.value
  }

  operator fun invoke(value: A): R = fromOrThrow(value)
}

class ExactException(error: Any) : IllegalArgumentException("ArrowExact error: $error")

interface Refined<A> {
  val value: A
}

