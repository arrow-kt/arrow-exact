package arrow.exact

import arrow.core.*
import arrow.core.raise.Raise
import arrow.core.raise.recover
import kotlin.jvm.JvmInline

@DslMarker
annotation class ExactBuilderDsl

@ExactBuilderDsl
fun <A, Constraint> exactBuilder(
  block: (ExactBuilder<A, Constraint>) -> Either<ExactError, Constraint>
): Exact<A, Constraint> {
  return object : Exact<A, Constraint> {
    override fun from(value: A): Either<ExactError, Constraint> =
      block(ExactBuilderImpl(value.right()))
  }
}

private class ExactBuilderImpl<A, Constraint>(
  private val value: Either<ExactError, A>
) : ExactBuilder<A, Constraint> {

  override fun mustBe(predicate: Predicate<A>): ExactBuilder<A, Constraint> = ExactBuilderImpl(
    value = value.flatMap {
      if (predicate(it)) it.right() else ExactError("Predicate failed").left()
    }
  )

  override fun <B> transform(transformation: (A) -> B): ExactBuilder<B, Constraint> = ExactBuilderImpl(
    value = value.map(transformation)
  )

  override fun <B> transformOrRaise(
    transformation: Raise<ExactError>.(A) -> B
  ): ExactBuilder<B, Constraint> = ExactBuilderImpl(
    value = value.flatMap {
      recover({ transformation(it).right() }) { ExactError("Transform or raise failed").left() }
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

@JvmInline
value class NotBlankTrimmedString private constructor(val value: String) {
  companion object : Exact<String, NotBlankTrimmedString> by exactBuilder({
    it.mustBe(String::isNotBlank)
      .transform(String::trim)
      .build(::NotBlankTrimmedString)
  })
}
