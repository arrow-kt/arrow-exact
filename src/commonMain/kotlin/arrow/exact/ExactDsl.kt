package arrow.exact

import arrow.core.Either
import arrow.core.raise.Raise
import arrow.core.raise.either

@DslMarker public annotation class ExactDsl

@ExactDsl
public fun <A, R> exact(construct: ExactScope<A, ExactError>.() -> R): Exact<A, R> =
  object : Exact<A, R> {
    override fun from(value: A): Either<ExactError, R> = either { construct(ExactScope(value, this)) }
  }

@ExactDsl
public fun <E : Any, A, R> exactEither(construct: ExactScope<A, E>.() -> R): ExactEither<E, A, R> =
  object : ExactEither<E, A, R> {
    override fun from(value: A): Either<E, R> = either { construct(ExactScope(value, this)) }
  }

public class ExactScope<A, E : Any>(public val raw: A, private val raise: Raise<E>) : Raise<E> by raise {

  @ExactDsl
  public fun <B> ensure(exact: ExactEither<E, A, B>): B {
    return when (val result = exact.from(raw)) {
      is Either.Left -> raise(result.value)
      is Either.Right -> result.value
    }
  }

  @ExactDsl
  public fun <B> ensure(exact: Exact<A, B>, error: () -> E): B {
    return when (val result = exact.from(raw)) {
      is Either.Left -> raise(error())
      is Either.Right -> result.value
    }
  }
}
