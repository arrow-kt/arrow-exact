package arrow.exact

import arrow.core.Either
import arrow.core.right

class NotBlankString private constructor(val str: String) {
  companion object : Exact<String, NotBlankString> by exact(String::isNotBlank, ::NotBlankString)
}

class NotBlankTrimmedString private constructor(val str: String) {

  private object TrimmedString : Exact<NotBlankString, NotBlankTrimmedString> {

    override fun fromOrEither(value: NotBlankString): Either<ExactError, NotBlankTrimmedString> {
      val trimmed = value.str.trim()
      return NotBlankTrimmedString(trimmed).right()
    }
  }

  companion object : Exact<String, NotBlankTrimmedString> by NotBlankString and TrimmedString
}
