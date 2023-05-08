package arrow.exact

import arrow.core.Either
import arrow.core.raise.Raise
import arrow.core.raise.either

@DslMarker
annotation class ExactDsl

@ExactDsl
fun <A, R> exact(
  construct: Raise<ExactError>.(A) -> R
): Exact<A, R> = object : Exact<A, R> {
  override fun from(value: A): Either<ExactError, R> = either { construct(value) }
}

@ExactDsl
fun <E : Any, A, R> exactEither(
  construct: Raise<E>.(A) -> R
): ExactEither<E, A, R> = object : ExactEither<E, A, R> {
  override fun from(value: A): Either<E, R> = either { construct(value) }
}
