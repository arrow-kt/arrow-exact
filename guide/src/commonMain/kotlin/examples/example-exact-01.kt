// This file was automatically generated from Exact.kt by Knit tool. Do not edit.
package arrow.exact.knit.example.exampleExact01

import arrow.core.raise.Raise
import arrow.exact.Exact
import arrow.exact.ExactError
import arrow.exact.ensure
import kotlin.jvm.JvmInline

@JvmInline
value class NotBlankString private constructor(val value: String) {
  companion object : Exact<String, NotBlankString> {
    override fun Raise<ExactError>.spec(raw: String): NotBlankString {
      ensure(raw.isNotBlank())
      return NotBlankString(raw)
    }
  }
}

fun example() {
  println(NotBlankString.from("Hello"))
  println(NotBlankString.from(""))
}
