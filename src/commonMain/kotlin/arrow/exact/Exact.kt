package arrow.exact

import arrow.core.Either
import arrow.core.raise.Raise
import arrow.core.raise.ensure

/**
 * <!--- TEST_NAME ExactExampleSpec -->
 *
 * Exact allows automatically projecting smart-constructors on a `Companion Object`. We can for
 * example easily create a `NotBlankString` type that is a `String` that is not blank, leveraging
 * the Arrow's [Raise] DSL to [ensure] the value is not blank.
 *
 * ```kotlin
 * import arrow.core.raise.ensure
 * import arrow.exact.Exact
 * import arrow.exact.ExactError
 * import arrow.exact.exact
 *
 * @JvmInline
 * value class NotBlankString private constructor(val value: String) {
 *   companion object : Exact<String, NotBlankString> by exact({
 *     ensure(raw.isNotBlank()) { ExactError("Cannot be blank.") }
 *     NotBlankString(raw)
 *   })
 * }
 * ```
 *
 * We can then easily create values of `NotBlankString` [from] a `String`, which returns us a
 * [Either] with the [ExactError] or the `NotBlankString`. We can also use [fromOrNull] to get a
 * nullable value, or [fromOrThrow] to throw an [ExactException].
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
 * Either.Left(ExactError(message=Cannot be blank.))
 * ```
 * <!--- KNIT example-exact-01.kt -->
 * <!--- TEST -->
 *
 * You can define a second type `NotBlankTrimmedString` that is a `NotBlankString` that is also
 * trimmed. Since the `exact` constructor allows us to compose `Exact` instances, we can easily
 * reuse the `NotBlankString` type.
 * <!--- INCLUDE
 * import arrow.core.raise.ensure
 * import arrow.exact.Exact
 * import arrow.exact.ExactError
 * import arrow.exact.exact
 *
 * @JvmInline value class NotBlankString private constructor(val value: String) {
 *   companion object : Exact<String, NotBlankString> by exact({
 *     ensure(raw.isNotBlank()) { ExactError("Cannot be blank.") }
 *     NotBlankString(raw)
 *   })
 * }
 * -->
 * ```kotlin
 * @JvmInline
 * value class NotBlankTrimmedString private constructor(val value: String) {
 *   companion object : Exact<String, NotBlankTrimmedString> by exact({
 *     val notBlank = ensure(NotBlankString)
 *     NotBlankTrimmedString(notBlank.value.trim())
 *   })
 * }
 * ```
 * <!--- KNIT example-exact-02.kt -->
 *
 * @see ExactEither if you need to return an [Either] with a custom error type.
 */
public fun interface Exact<A, out R> : ExactEither<ExactError, A, R>

// TODO: Should we just use `String` ???
public data class ExactError(val message: String)

/**
 * A more generic version of [Exact] that allows working over a custom error type rather than
 * [ExactError]. Since [Exact] is a specialization of [ExactEither], where [E] is fixed to
 * [ExactError], we can easily combine the two by mapping from [ExactError] to our custom [E] type.
 *
 * <!--- INCLUDE
 * import arrow.core.raise.ensure
 * import arrow.exact.Exact
 * import arrow.exact.ExactEither
 * import arrow.exact.ExactError
 * import arrow.exact.exact
 * import arrow.exact.exactEither
 *
 * @JvmInline value class NotBlankTrimmedString private constructor(val value: String) {
 *   companion object : Exact<String, NotBlankTrimmedString> by exact({
 *     ensure(raw.isNotBlank()) { ExactError("Cannot be blank.") }
 *     NotBlankTrimmedString(raw.trim())
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
 *   companion object : ExactEither<UsernameError, String, Username> by exactEither({
 *     val username =
 *       ensure(NotBlankTrimmedString) {
 *         UsernameError.Invalid
 *       }.value
 *     ensure(username.length < 100) { UsernameError.Invalid }
 *     ensure(username !in listOf("offensive")) { UsernameError.Offensive(username) }
 *     Username(username)
 *   })
 * }
 * ```
 * <!--- KNIT example-exact-03.kt -->
 */
public fun interface ExactEither<out E : Any, A, out R> {

  public fun from(value: A): Either<E, R>

  public fun fromOrNull(value: A): R? = from(value).getOrNull()

  public fun fromOrThrow(value: A): R =
    when (val result = from(value)) {
      is Either.Left -> throw ExactException(result.value)
      is Either.Right -> result.value
    }
}

/** The exception that is thrown by [ExactEither.fromOrThrow] when the value is invalid. */
public class ExactException(error: Any) : IllegalArgumentException("ArrowExact error: $error")
