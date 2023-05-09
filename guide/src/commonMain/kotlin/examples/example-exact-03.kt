// This file was automatically generated from Exact.kt by Knit tool. Do not edit.
package arrow.exact.knit.example.exampleExact03

import arrow.core.raise.ensure
import arrow.exact.Exact
import arrow.exact.ExactEither
import arrow.exact.ExactError
import arrow.exact.exact
import arrow.exact.exactEither

@JvmInline value class NotBlankTrimmedString private constructor(val value: String) {
  companion object : Exact<String, NotBlankTrimmedString> by exact({ raw ->
    ensure(raw.isNotBlank()) { ExactError("Cannot be blank.") }
    NotBlankTrimmedString(raw.trim())
  })
}

sealed interface UsernameError {
  object Invalid : UsernameError
  data class Offensive(val username: String) : UsernameError
}

@JvmInline
value class Username private constructor(val value: String) {
  companion object : ExactEither<UsernameError, String, Username> by exactEither({ rawUsername ->
      val username =
        NotBlankTrimmedString.from(rawUsername)
          .mapLeft { UsernameError.Invalid }
          .bind()
          .value
      ensure(username.length < 100) { UsernameError.Invalid }
      ensure(username !in listOf("offensive")) { UsernameError.Offensive(username) }
      Username(username)
  })
}
