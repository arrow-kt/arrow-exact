// This file was automatically generated from Exact.kt by Knit tool. Do not edit.
package arrow.exact.knit.example.exampleExact03

import arrow.exact.Exact
import arrow.exact.ensure
import kotlin.jvm.JvmInline

class NotBlankString private constructor(val value: String) {
  companion object : Exact<String, NotBlankString> by Exact({
    ensure(it.isNotBlank())
    NotBlankString(it)
  })
}

@JvmInline
value class NotBlankTrimmedString private constructor(val value: String) {
  companion object : Exact<String, NotBlankTrimmedString> by Exact({
    ensure(it, NotBlankString)
    NotBlankTrimmedString(it.trim())
  })
}
