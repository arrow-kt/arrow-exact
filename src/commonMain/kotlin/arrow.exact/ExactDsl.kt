package arrow.exact

import arrow.core.*
import arrow.core.raise.Raise
import arrow.core.raise.either

internal class AndExact<A, B, C>(
  private val exact1: Exact<A, B>, private val exact2: Exact<B, C>
) : Exact<A, C> {

  override fun from(value: A): Either<ExactError, C> {
    return exact1.from(value).flatMap { exact2.from(it) }
  }
}

fun <A, B> exact(predicate: Predicate<A>, constructor: (A) -> B): Exact<A, B> {
  return object : Exact<A, B> {
    override fun from(value: A): Either<ExactError, B> {
      return if (predicate.invoke(value)) {
        constructor.invoke(value).right()
      } else {
        ExactError("Value ($value) doesn't match the predicate").left()
      }
    }
  }
}

fun <A, B> exact(constraint: Raise<ExactError>.(A) -> B): Exact<A, B> {
  return object : Exact<A, B> {
    override fun from(value: A): Either<ExactError, B> = either { constraint(value) }
  }
}


infix fun <A, B, C> Exact<A, B>.and(other: Exact<B, C>): Exact<A, C> {
  return AndExact(this, other)
}
