package arrow.exact

import arrow.core.raise.Raise

@DslMarker
annotation class ExactDsl

@ExactDsl
fun <A, R : Refined<A>> exact(
  construct: Raise<ExactError>.(A) -> R
): Exact<A, R> = object : Exact<A, R> {
  override fun Raise<ExactError>.from(value: A): R = construct(value)
}


@ExactDsl
fun <A, E : Any, R : Refined<A>> exactWithError(
  construct: Raise<E>.(A) -> R
): ExactWithError<A, E, R> = object : ExactWithError<A, E, R> {
  override fun Raise<E>.from(value: A): R = construct(value)
}
