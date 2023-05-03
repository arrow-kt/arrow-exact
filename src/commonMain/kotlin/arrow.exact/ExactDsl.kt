package arrow.exact

import arrow.core.*

internal class AndExact<A, B, C>(
  private val exact1: Exact<A, B>,
  private val exact2: Exact<B, C>
) : Exact<A, C> {

  override fun fromOrEither(value: A): Either<ExactError, C> {
    return exact1.fromOrEither(value)
      .flatMap { exact2.fromOrEither(it) }
  }
}

fun <A, B> exact(predicate: Predicate<A>, constructor: (A) -> B): Exact<A, B> {
  return object : Exact<A, B> {
    override fun fromOrEither(value: A): Either<ExactError, B> {
      return if (predicate.invoke(value)) {
        constructor.invoke(value).right()
      } else {
        ExactError("Value ($value) doesn't match the predicate").left()
      }
    }
  }
}

infix fun <A, B, C> Exact<A, B>.and(other: Exact<B, C>): Exact<A, C> {
  return AndExact(this, other)
}
