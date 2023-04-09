package number

import arrow.core.Some
import io.kotest.assertions.arrow.core.shouldBeNone
import io.kotest.assertions.arrow.core.shouldBeSome
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.doubles.beWithinPercentageOf
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll

class PositiveDoubleDoubleTest : FreeSpec({
  "[PROPERTY] ∀ PositiveDoubleDouble is a PositiveDouble and finite number" {
    checkAll(Arb.double()) { double ->
      val option = PositiveDouble.fromDouble(double)

      if (option is Some) {
        val wrappedValue = option.value.value
        wrappedValue shouldBeGreaterThan 0.0
        wrappedValue.isFinite().shouldBeTrue()
      }
    }
  }

  "[UNHAPPY] Unsafe calls throws understandable exception" {
    val err = shouldThrowExactly<ArrowExactPositiveDoubleConstraintError> {
      PositiveDouble.unsafe(-3.14)
    }

    err.violatingValue shouldBe -3.14
    err.message shouldContain "ArrowExact"
    err.message shouldContain "PositiveDouble"
    err.message shouldContain "-3.14"
    err.message shouldContain "finite"
    err.message shouldContain "positive"
  }

  "fromDouble" - {
    "[EDGE] Double.PositiveDouble_INFINITY is None" {
      val res = PositiveDouble.fromDouble(Double.POSITIVE_INFINITY)

      res.shouldBeNone()
    }

    "[EDGE] Double.NEGATIVE_INFINITY is None" {
      val res = PositiveDouble.fromDouble(Double.NEGATIVE_INFINITY)

      res.shouldBeNone()
    }
  }

  "Addition" - {
    "[HAPPY] 1 + 2 = 3" {
      val res = PositiveDouble(1.0) + PositiveDouble(2.0)
      res shouldBeSome PositiveDouble(3.0)
    }

    "[EDGE] Double.MAX_VALUE + 1 = Double.MAX_VALUE" {
      val res = PositiveDouble(Double.MAX_VALUE) + PositiveDouble(1.0)

      res shouldBeSome PositiveDouble(Double.MAX_VALUE)
    }

    "[EDGE] Double.MAX_VALUE + some big number = Double.MAX_VALUE" {
      val res = PositiveDouble(Double.MAX_VALUE) + PositiveDouble(1_000_000_000_000_000.0)

      res shouldBeSome PositiveDouble(Double.MAX_VALUE)
    }

    "[EDGE] Double.MAX_VALUE + Double.MAX_VALUE is None" {
      val res = PositiveDouble(Double.MAX_VALUE) + PositiveDouble(Double.MAX_VALUE)

      res.shouldBeNone()
    }
  }

  "Multiplication" - {
    "[HAPPY] 2 * 3 = 6" {
      val res = PositiveDouble(2.0) * PositiveDouble(3.0)
      res shouldBeSome PositiveDouble(6.0)
    }

    "[EDGE] Double.MAX_VALUE * 0.5 = Double.MAX_VALUE / 2" {
      val res = PositiveDouble(Double.MAX_VALUE) * PositiveDouble(0.5)
      res.shouldBeSome()
      res.value.value shouldBe beWithinPercentageOf(Double.MAX_VALUE / 2, 1.0)
    }

    "[EDGE] Double.MAX_VALUE * 2 is None" {
      val res = PositiveDouble(Double.MAX_VALUE) * PositiveDouble(2.0)
      res.shouldBeNone()
    }

    "[EDGE] Double.MAX_VALUE * Double.MAX_VALUE is None" {
      val res = PositiveDouble(Double.MAX_VALUE) * PositiveDouble(Double.MAX_VALUE)
      res.shouldBeNone()
    }
  }

})
