package number

import arrow.core.Some
import io.kotest.assertions.arrow.core.shouldBeNone
import io.kotest.assertions.arrow.core.shouldBeSome
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll

class PositiveDoubleTest : FreeSpec({
  "[PROPERTY] ∀ PositiveDouble is a positive and finite number" {
    checkAll(Arb.double()) { double ->
      val positiveDouble = PositiveDouble.fromDouble(double)

      if (positiveDouble is Some) {
        val wrappedValue = positiveDouble.value.value
        wrappedValue shouldBeGreaterThan 0.0
        wrappedValue.isFinite().shouldBeTrue()
      }
    }
  }

  "[UNHAPPY] Unsafe calls throws understandable exception" {
    val err = shouldThrow<ArrowExactPositiveDoubleConstraintError> {
      PositiveDouble.unsafe(-3.14)
    }

    err.violatingValue shouldBe -3.14
    err.message shouldContain "ArrowExact"
    err.message shouldContain "PositiveDouble"
    err.message shouldContain "-3.14"
    err.message shouldContain "finite"
    err.message shouldContain "positive"
  }

  // region Edge cases
  "[EDGE] Double.MAX_VALUE * Double.MAX_VALUE is None" {
    val res = positive(Double.MAX_VALUE) * positive(Double.MAX_VALUE)
    res.shouldBeNone()
  }

  "[EDGE] Double.MAX_VALUE + 1 = Double.MAX_VALUE" {
    val res = positive(Double.MAX_VALUE) + positive(1.0)

    res.shouldBeSome(positive(Double.MAX_VALUE))
  }

  "[EDGE] Double.MAX_VALUE + 100 = Double.MAX_VALUE" {
    val res = positive(Double.MAX_VALUE) + positive(1_000.0)

    res.shouldBeSome(positive(Double.MAX_VALUE))
  }

  "[EDGE] Double.MAX_VALUE + Double.MAX_VALUE is None" {
    val res = positive(Double.MAX_VALUE) + positive(Double.MAX_VALUE)

    res.shouldBeNone()
  }

  "[EDGE] Double.POSITIVE_INFINITY is None" {
    val res = PositiveDouble.fromDouble(Double.POSITIVE_INFINITY)

    res.shouldBeNone()
  }

  "[EDGE] Double.NEGATIVE_INFINITY is None" {
    val res = PositiveDouble.fromDouble(Double.NEGATIVE_INFINITY)

    res.shouldBeNone()
  }
  // endregion
})

fun positive(double: Double): PositiveDouble = PositiveDouble(double)
