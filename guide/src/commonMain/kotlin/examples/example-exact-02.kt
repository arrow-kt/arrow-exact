// This file was automatically generated from Exact.kt by Knit tool. Do not edit.
package arrow.exact.knit.example.exampleExact02

import arrow.exact.Exact
import arrow.exact.ensure

@JvmInline
value class NotBlankString private constructor(val value: String) {
  companion object : Exact<String, NotBlankString> by Exact({
    ensure(it.isNotBlank())
    NotBlankString(it)
  })
}
