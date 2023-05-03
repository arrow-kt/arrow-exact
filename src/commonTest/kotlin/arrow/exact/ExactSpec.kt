package arrow.exact

import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class ExactSpec : StringSpec({
  "creates NotBlankTrimmedString" {
    val notBlank = NotBlankTrimmedString.fromOrThrow(" test  ")
    notBlank.str shouldBe "test"
  }

  "throws exception on failed check" {
    shouldThrow<IllegalArgumentException> {
      NotBlankTrimmedString.fromOrThrow("  ")
    }
  }

  "returns not null" {
    NotBlankTrimmedString.fromOrNull("test") shouldNotBe null
  }

  "returns null" {
    NotBlankTrimmedString.fromOrNull("  ") shouldBe null
  }

  "returns right" {
    val either = NotBlankTrimmedString.fromOrEither("  test  ")
    either.map { it.str } shouldBeRight "test"
  }

  "returns left" {
    NotBlankTrimmedString.fromOrEither("    ").isLeft() shouldBe true
  }
})
