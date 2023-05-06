package arrow.exact.demo

import arrow.core.raise.ensureNotNull
import arrow.exact.Exact
import arrow.exact.ExactError
import arrow.exact.Refined
import arrow.exact.exact
import kotlin.jvm.JvmInline

@JvmInline
value class NotBlankTrimmedString private constructor(
  override val value: String
) : Refined<String> {
  companion object : Exact<String, NotBlankTrimmedString> by exact({
    val trimmed = it.trim().takeIf(String::isNotBlank)
    ensureNotNull(trimmed) { ExactError("Cannot be blank.") }
    NotBlankTrimmedString(trimmed)
  })
}

fun main() {
  val str = NotBlankTrimmedString("Hello")

}
