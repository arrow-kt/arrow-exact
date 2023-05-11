// This file was automatically generated from Exact.kt by Knit tool. Do not edit.
package arrow.exact.knit.example.exampleExact02

import arrow.core.raise.ensure
import arrow.exact.Exact
import arrow.exact.ExactError
import arrow.exact.exact

@JvmInline value class NotBlankString private constructor(val value: String) {
  companion object : Exact<String, NotBlankString> by exact({
    ensure(raw.isNotBlank()) { ExactError("Cannot be blank.") }
    NotBlankString(raw)
  })
}

@JvmInline
value class NotBlankTrimmedString private constructor(val value: String) {
  companion object : Exact<String, NotBlankTrimmedString> by exact({
    val notBlank = ensure(NotBlankString)
    NotBlankTrimmedString(notBlank.value.trim())
  })
}
