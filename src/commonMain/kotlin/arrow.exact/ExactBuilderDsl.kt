package arrow.exact

import arrow.core.*
import arrow.core.raise.Raise
import arrow.core.raise.either

@DslMarker
annotation class ExactBuilderDsl

@ExactBuilderDsl
fun <A, Constraint> exactBuilder(
  build: (ExactBuilder<A, Constraint>) -> Either<ExactError, Constraint>
): Exact<A, Constraint> {
  return object : Exact<A, Constraint> {
    override fun from(value: A): Either<ExactError, Constraint> =
      build(ExactBuilderImpl(value.right()))
  }
}

private class ExactBuilderImpl<A, Constraint>(
  private val value: Either<ExactError, A>
) : ExactBuilder<A, Constraint> {

  override fun mustBe(predicate: Predicate<A>): ExactBuilder<A, Constraint> = ExactBuilderImpl(
    value = value.flatMap { a ->
      if (predicate(a)) a.right() else ExactError("Predicate failed for value: $a").left()
    }
  )

  override fun <B> transform(transformation: (A) -> B): ExactBuilder<B, Constraint> = ExactBuilderImpl(
    value = value.map(transformation)
  )

  override fun <B> transformOrRaise(
    transformation: Raise<ExactError>.(A) -> B
  ): ExactBuilder<B, Constraint> = ExactBuilderImpl(
    value = value.flatMap { a ->
      either { transformation(a) }
    }
  )

  override fun build(constructor: (A) -> Constraint): Either<ExactError, Constraint> = value.map(constructor)
}

interface ExactBuilder<A, Constraint> {
  @ExactBuilderDsl
  fun mustBe(predicate: Predicate<A>): ExactBuilder<A, Constraint>

  @ExactBuilderDsl
  fun <B> transform(transformation: (A) -> B): ExactBuilder<B, Constraint>

  @ExactBuilderDsl
  fun <B> transformOrRaise(transformation: Raise<ExactError>.(A) -> B): ExactBuilder<B, Constraint>

  @ExactBuilderDsl
  fun build(constructor: (A) -> Constraint): Either<ExactError, Constraint>
}
