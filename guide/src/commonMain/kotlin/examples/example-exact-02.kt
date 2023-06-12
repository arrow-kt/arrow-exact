// This file was automatically generated from Exact.kt by Knit tool. Do not edit.
package arrow.exact.knit.example.exampleExact02

import arrow.core.raise.Raise
import arrow.exact.Exact
import arrow.core.raise.ensure
import kotlin.jvm.JvmInline

@JvmInline
value class NotBlankString private constructor(val value: String) {
  companion object : Exact<String, NotBlankString> {
    override fun Raise<String>.spec(raw: String): NotBlankString {
      ensure(raw.isNotBlank()) { "String must not be blank." }
      return NotBlankString(raw)
    }
  }
}

fun example() {
  println(NotBlankString.from(""))
}
