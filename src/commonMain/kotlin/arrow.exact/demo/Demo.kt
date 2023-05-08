package arrow.exact.demo

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.exact.*
import kotlin.jvm.JvmInline

// TODO: We need a lint check telling people to make their constructors private
@JvmInline
public value class NotBlankString private constructor(public val value: String) {
  public companion object :
    Exact<String, NotBlankString> by exact({
      ensure(it.isNotBlank()) { ExactError("Cannot be blank.") }
      NotBlankString(it)
    })
}

// TODO: We need a lint check telling people to make their constructors private
@JvmInline
public value class NotBlankTrimmedString private constructor(public val value: String) {
  public companion object :
    Exact<String, NotBlankTrimmedString> by exact({ raw ->
      val notBlank = NotBlankString.from(raw).bind()
      NotBlankTrimmedString(notBlank.value.trim())
    })
}

public sealed interface UsernameError {
  public object Invalid : UsernameError
  public data class Offensive(val username: String) : UsernameError
}

@JvmInline
public value class Username private constructor(public val value: String) {
  public companion object :
    ExactEither<UsernameError, String, Username> by exactEither({ rawUsername ->
      val username =
        NotBlankTrimmedString.from(rawUsername) // compose Exact
          .mapLeft { UsernameError.Invalid }
          .bind()
          .value
      ensure(username.length < 100) { UsernameError.Invalid }
      ensure(username !in listOf("offensive")) { UsernameError.Offensive(username) }
      Username(username)
    })
}

@JvmInline
public value class PositiveInt private constructor(public val value: Int) {
  public companion object :
    Exact<Int, PositiveInt> by exact({
      ensure(it > 0) { ExactError("Must be positive.") }
      PositiveInt(it)
    })
}

public fun demo(): Either<String, Unit> = either {
  val hello = NotBlankTrimmedString.fromOrThrow("Hello")
  val world = NotBlankTrimmedString.fromOrThrow("World")

  val helloWorld =
    NotBlankTrimmedString.from(hello.value + " " + world.value).mapLeft { it.message }.bind()

  val username = Username.from("user1").mapLeft { it.toString() }.bind()
}
