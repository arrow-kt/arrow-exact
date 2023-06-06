// This file was automatically generated from Exact.kt by Knit tool. Do not edit.
package arrow.exact.knit.example.exampleExact04

import arrow.core.raise.Raise
import arrow.exact.Exact
import arrow.exact.ensure
import kotlin.jvm.JvmInline

class NotBlankString private constructor(val value: String) {
  companion object : Exact<String, NotBlankString> {
    override fun Raise<String>.spec(raw: String): NotBlankString {
      ensure(raw.isNotBlank())
      return NotBlankString(raw)
    }
  }
}

@JvmInline
value class NotBlankTrimmedString private constructor(val value: String) {
  companion object : Exact<String, NotBlankTrimmedString> {
    override fun Raise<String>.spec(raw: String): NotBlankTrimmedString {
      ensure(raw, NotBlankString)
      return NotBlankTrimmedString(raw.trim())
    }
  }
}
