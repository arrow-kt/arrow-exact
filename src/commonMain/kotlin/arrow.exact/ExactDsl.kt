package arrow.exact

import arrow.core.Either
import arrow.core.raise.Raise
import arrow.core.raise.either

@DslMarker
annotation class ExactDsl

@ExactDsl
fun <A, R : Refined<A>> exact(
  construct: Raise<ExactError>.(A) -> R
): Exact<A, R> = object : Exact<A, R> {
  override fun from(value: A): Either<ExactError, R> = either { construct(value) }
}


@ExactDsl
fun <A, E : Any, R : Refined<A>> exactWithError(
  construct: Raise<E>.(A) -> R
): ExactWithError<A, E, R> = object : ExactWithError<A, E, R> {
  override fun from(value: A): Either<E, R> = either { construct(value) }
}
