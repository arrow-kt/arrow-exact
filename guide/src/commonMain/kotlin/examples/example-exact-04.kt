// This file was automatically generated from Exact.kt by Knit tool. Do not edit.
package arrow.exact.knit.example.exampleExact04

import arrow.core.raise.ensure
import arrow.exact.Exact
import arrow.exact.ExactEither
import arrow.exact.ensure
import kotlin.jvm.JvmInline

@JvmInline
value class NotBlankTrimmedString private constructor(val value: String) {
  companion object : Exact<String, NotBlankTrimmedString> by Exact({
    ensure(it.isNotBlank())
    NotBlankTrimmedString(it.trim())
  })
}

sealed interface UsernameError {
  object Invalid : UsernameError
  data class Offensive(val username: String) : UsernameError
}

@JvmInline
value class Username private constructor(val value: String) {
  companion object : ExactEither<UsernameError, String, Username> by ExactEither({
    val username =
      ensure(it, NotBlankTrimmedString) {
        UsernameError.Invalid
      }.value
    ensure(username.length < 100) { UsernameError.Invalid }
    ensure(username !in listOf("offensive")) { UsernameError.Offensive(username) }
    Username(username)
  })
}
