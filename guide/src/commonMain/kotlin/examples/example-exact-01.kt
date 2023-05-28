// This file was automatically generated from Exact.kt by Knit tool. Do not edit.
package arrow.exact.knit.example.exampleExact01

import arrow.core.raise.Raise
import arrow.core.raise.ensure
import arrow.exact.Exact
import arrow.exact.ExactError

@JvmInline
value class NotBlankString private constructor(val value: String) {
  companion object : Exact<String, NotBlankString>() {
    override fun Raise<ExactError>.spec(raw: String): NotBlankString {
      ensure(raw.isNotBlank()) { ExactError("Cannot be blank.") }
      return NotBlankString(raw)
    }
  }
}

fun example() {
  println(NotBlankString.from("Hello"))
  println(NotBlankString.from(""))
}
