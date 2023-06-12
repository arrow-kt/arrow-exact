// This file was automatically generated from Exact.kt by Knit tool. Do not edit.
package arrow.exact.knit.example.exampleExact05

import arrow.core.raise.Raise
import arrow.core.raise.ensure
import arrow.exact.Exact
import arrow.exact.ExactEither
import arrow.exact.ensure
import kotlin.jvm.JvmInline

@JvmInline
value class NotBlankTrimmedString private constructor(val value: String) {
  companion object : Exact<String, NotBlankTrimmedString> {
    override fun Raise<String>.spec(raw: String): NotBlankTrimmedString {
      ensure(raw.isNotBlank())
      return NotBlankTrimmedString(raw.trim())
    }
  }
}

sealed interface UsernameError {
  object Invalid : UsernameError
  data class Offensive(val username: String) : UsernameError
}

@JvmInline
value class Username private constructor(val value: String) {
  companion object : ExactEither<UsernameError, String, Username> {
    override fun Raise<UsernameError>.spec(raw: String): Username {
      val username =
        ensure(raw, NotBlankTrimmedString) {
          UsernameError.Invalid
        }.value
      ensure(username.length < 100) { UsernameError.Invalid }
      ensure(username !in listOf("offensive")) { UsernameError.Offensive(username) }
      return Username(username)
    }
  }
}
