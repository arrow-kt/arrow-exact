package arrow.exact.demo

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.right
import arrow.exact.*
import kotlin.jvm.JvmInline
import kotlin.random.Random

// TODO: We need a lint check telling people to make their constructors private
@JvmInline
value class NotBlankString private constructor(
  val value: String
) {
  companion object : Exact<String, NotBlankString> by exact({
    ensure(it.isNotBlank()) { ExactError("Cannot be blank.") }
    NotBlankString(it)
  })
}

// TODO: We need a lint check telling people to make their constructors private
@JvmInline
value class NotBlankTrimmedString private constructor(
  val value: String
) {
  companion object : Exact<String, NotBlankTrimmedString> by exact({ raw ->
    val notBlank = NotBlankString.from(raw).bind()
    NotBlankTrimmedString(notBlank.value.trim())
  })
}

sealed interface UsernameError {
  object Invalid : UsernameError
  data class Offensive(val username: String) : UsernameError
}

@JvmInline
value class Username private constructor(
  val value: String
) {
  companion object : ExactEither<UsernameError, String, Username> by exactEither({ rawUsername ->
    val username = NotBlankTrimmedString.from(rawUsername) // compose Exact
      .mapLeft { UsernameError.Invalid }.bind().value
    ensure(username.length < 100) { UsernameError.Invalid }
    ensure(username !in listOf("offensive")) { UsernameError.Offensive(username) }
    Username(username)
  })
}

@JvmInline
value class PositiveInt private constructor(
  val value: Int
) {
  companion object : Exact<Int, PositiveInt> by exact({
    ensure(it > 0) { ExactError("Must be positive.") }
    PositiveInt(it)
  })
}

fun demo(): Either<String, Unit> = either {
  val hello = NotBlankTrimmedString("Hello")
  val world = NotBlankTrimmedString("World")

  val helloWorld = NotBlankTrimmedString.from(hello.value + " " + world.value)
    .mapLeft { it.message }.bind()
  val username = Username.from("user1")
    .mapLeft { it.toString() }.bind()


  val x = PositiveInt(3)
  val y = PositiveInt.from(Random.nextInt())
  val z = y.flatMap { y1 ->
    PositiveInt(x.value + y1.value).right()
  }
}
