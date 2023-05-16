// This file was automatically generated from README.md by Knit tool. Do not edit.
package arrow.exact.knit.example.exampleReadme01

import arrow.core.raise.ensure
import arrow.exact.Exact
import arrow.exact.ExactError
import arrow.exact.exact

@JvmInline
value class NotBlankString private constructor(val value: String) {
  companion object : Exact<String, NotBlankString> by exact({
    ensure(raw.isNotBlank()) { ExactError("Cannot be blank.") }
    NotBlankString(raw)
  })
}

fun example() {
  println(NotBlankString.from("Hello"))
  println(NotBlankString.from(""))
}
