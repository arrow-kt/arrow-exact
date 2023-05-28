// This file was automatically generated from README.md by Knit tool. Do not edit.
package arrow.exact.knit.example.exampleReadme02

import arrow.core.raise.Raise
import arrow.core.raise.ensure
import arrow.exact.Exact
import arrow.exact.ExactError
import arrow.exact.ensure

@JvmInline
value class NotBlankString private constructor(val value: String) {
  companion object : Exact<String, NotBlankString> {
    override fun Raise<ExactError>.spec(raw: String): NotBlankString {
      ensure(raw.isNotBlank()) { ExactError("Cannot be blank.") }
      return NotBlankString(raw)
    }
  }
}

@JvmInline
value class NotBlankTrimmedString private constructor(val value: String) { 
  companion object : Exact<String, NotBlankTrimmedString> { 
    override fun Raise<ExactError>.spec(raw: String): NotBlankTrimmedString { 
      ensure(raw, NotBlankString)
      return NotBlankTrimmedString(raw.trim())
    }
  }
}
