package arrow.exact

import arrow.core.Either
import arrow.core.raise.Raise
import arrow.core.raise.either
import arrow.core.raise.ensure
import kotlin.jvm.JvmInline

/**
 * <!--- TEST_NAME ExactExampleSpec -->
 *
 * Exact allows automatically projecting smart-constructors on a `Companion Object`. We can for
 * example easily create a `NotBlankString` type that is a `String` that is not blank, leveraging
 * the Arrow's [Raise] DSL to [ensure] the value is not blank.
 *
 * ```kotlin
 * import arrow.core.raise.Raise
 * import arrow.exact.Exact
 * import arrow.exact.ErrorMessage
 * import arrow.exact.ensure
 * import kotlin.jvm.JvmInline
 *
 * @JvmInline
 * value class NotBlankString private constructor(val value: String) {
 *   companion object : Exact<String, NotBlankString> {
 *     override fun Raise<ErrorMessage>.spec(raw: String): NotBlankString {
 *       ensure(raw.isNotBlank())
 *       return NotBlankString(raw)
 *     }
 *   }
 * }
 * ```
 *
 * We can then easily create values of `NotBlankString` [ExactEither.from] a `String`, which returns us a
 * [Either] with the [ErrorMessage] or the `NotBlankString`. We can also use [ExactEither.fromOrNull] to get a
 * nullable value, or [ExactEither.fromOrThrow] to throw an [ExactException].
 *
 * **note:** Make sure to define your constructor as `private` to prevent creating invalid values.
 *
 * ```kotlin
 * fun example() {
 *   println(NotBlankString.from("Hello"))
 *   println(NotBlankString.from(""))
 * }
 * ```
 *
 * The output of the above program is:
 * ```text
 * Either.Right(NotBlankString(value=Hello))
 * Either.Left(ErrorMessage(message=Failed condition.))
 * ```
 * <!--- KNIT example-exact-01.kt -->
 * <!--- TEST -->
 *
 * You can also define [Exact] by using Kotlin delegation.
 * <!--- INCLUDE
 * import arrow.exact.Exact
 * import arrow.exact.ensure
 * import kotlin.jvm.JvmInline
 * -->
 * ```kotlin
 * @JvmInline
 * value class NotBlankString private constructor(val value: String) {
 *   companion object : Exact<String, NotBlankString> by Exact({
 *     ensure(it.isNotBlank())
 *     NotBlankString(it)
 *   })
 * }
 * ```
 * <!--- KNIT example-exact-02.kt -->
 *
 * You can define a second type `NotBlankTrimmedString` that is a `NotBlankString` that is also
 * trimmed. [ensure] allows us to compose [Exact] instances and easily
 * reuse the `NotBlankString` type.
 * <!--- INCLUDE
 * import arrow.exact.Exact
 * import arrow.exact.ensure
 * import kotlin.jvm.JvmInline
 *
 * class NotBlankString private constructor(val value: String) {
 *   companion object : Exact<String, NotBlankString> by Exact({
 *     ensure(it.isNotBlank())
 *     NotBlankString(it)
 *   })
 * }
 * -->
 * ```kotlin
 * @JvmInline
 * value class NotBlankTrimmedString private constructor(val value: String) {
 *   companion object : Exact<String, NotBlankTrimmedString> by Exact({
 *     ensure(it, NotBlankString)
 *     NotBlankTrimmedString(it.trim())
 *   })
 * }
 * ```
 * <!--- KNIT example-exact-03.kt -->
 *
 * @see ExactEither if you need to return an [Either] with a custom error type.
 */
public typealias Exact<Input, Output> = ExactEither<ErrorMessage, Input, Output>

@JvmInline
public value class ErrorMessage(public val message: String)

/**
 * Input more generic version of [Exact] that allows working over a custom error type rather than
 * [ErrorMessage]. Since [Exact] is a specialization of [ExactEither], where [Error] is fixed to
 * [ErrorMessage], we can easily combine the two by mapping from [ErrorMessage] to our custom [Error] type.
 *
 * <!--- INCLUDE
 * import arrow.core.raise.ensure
 * import arrow.exact.Exact
 * import arrow.exact.ExactEither
 * import arrow.exact.ensure
 * import kotlin.jvm.JvmInline
 *
 * @JvmInline
 * value class NotBlankTrimmedString private constructor(val value: String) {
 *   companion object : Exact<String, NotBlankTrimmedString> by Exact({
 *     ensure(it.isNotBlank())
 *     NotBlankTrimmedString(it.trim())
 *   })
 * }
 * -->
 * ```kotlin
 * sealed interface UsernameError {
 *   object Invalid : UsernameError
 *   data class Offensive(val username: String) : UsernameError
 * }
 *
 * @JvmInline
 * value class Username private constructor(val value: String) {
 *   companion object : ExactEither<UsernameError, String, Username> by ExactEither({
 *     val username =
 *       ensure(it, NotBlankTrimmedString) {
 *         UsernameError.Invalid
 *       }.value
 *     ensure(username.length < 100) { UsernameError.Invalid }
 *     ensure(username !in listOf("offensive")) { UsernameError.Offensive(username) }
 *     Username(username)
 *   })
 * }
 * ```
 * <!--- KNIT example-exact-04.kt -->
 */
public fun interface ExactEither<out Error : Any, in Input, out Output> {

  public fun Raise<Error>.spec(raw: Input): Output

  public fun from(value: Input): Either<Error, Output> = either { spec(value) }

  public fun fromOrNull(value: Input): Output? = from(value).getOrNull()

  public fun fromOrThrow(value: Input): Output =
    when (val result = from(value)) {
      is Either.Left -> throw ExactException(result.value)
      is Either.Right -> result.value
    }
}

/** The exception that is thrown by [ExactEither.fromOrThrow] when the value is invalid. */
public class ExactException(error: Any) : IllegalArgumentException("ArrowExact error: $error")
