package arrow.exact.demo

import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import arrow.exact.*
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

sealed interface UsernameError {
  object Invalid : UsernameError
  data class Offensive(val username: String) : UsernameError
}

@JvmInline
value class Username private constructor(
  override val value: String
) : Refined<String> {
  companion object : ExactEither<UsernameError, String, Username> by exactEither({
    ensure(it.isNotBlank() && it.length < 100) { UsernameError.Invalid }
    ensure(it !in listOf("offensive")) { UsernameError.Offensive(it) }
    Username(it.trim())
  })
}

fun main() {
  val hello = NotBlankTrimmedString("Hello")
  val world = NotBlankTrimmedString("World")

  val helloWorld = NotBlankTrimmedString.fromOrNull(hello.value + " " + world.value)
}
