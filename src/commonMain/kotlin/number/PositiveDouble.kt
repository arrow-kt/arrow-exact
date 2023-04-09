package number

import ArrowExactConstraintViolationError
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import number.PositiveDouble.Companion.fromDouble
import number.PositiveDouble.Companion.invoke
import kotlin.jvm.JvmInline

/**
 * Represents a positive and finite [Double] value in (0, [Double.MAX_VALUE]].
 * **Invariant (property):** The PositiveDouble [value] will always be a finite and positive number.
 * It also overrides the arithmetics operators **addition "+"** [plus], **subtraction "-"** [minus],
 * __multiplication "*"__ [times], **division "/"** [div] and **remainder "%"** [rem] in a type-safe way.
 *
 * All operation that return [Option]<PositiveDouble> are type-safe and return [Some] when
 * the wrapped value is positive and finite and [None] when the invariant is violated.
 *
 * ### Usage:
 * - Use the [fromDouble] function to safely create a [PositiveDouble] instance.
 * - The [invoke] or [PositiveDouble.unsafe] functions are an unsafe alternative which throws
 * an [ArrowExactPositiveDoubleConstraintError] for invalid input.
 *
 * ### Examples:
 * ```
 * // Happy path
 * val result = PositiveDouble.fromDouble(5.0) // Some(Positive(5.0))
 *
 * // Unhappy path
 * val result = PositiveDouble.fromDouble(-5.0) // None
 *
 * // Edge cases
 * val maxResult = PositiveDouble.fromDouble(Double.MAX_VALUE) // Some(Positive(Double.MAX_VALUE))
 * val notFiniteResult = PositiveDouble.fromDouble(Double.POSITIVE_INFINITY) // None
 *
 * // Addition and subtraction examples
 * val a = PositiveDouble.fromDouble(2.0) // Some(Positive(2.0))
 * val b = PositiveDouble.fromDouble(3.0) // Some(Positive(3.0))
 * val additionResult = a + b // Some(Positive(5.0))
 * val subtractionResult = a - b // None
 * ```
 *
 * @property value The wrapped positive and finite [Double] value.
 */
@JvmInline
value class PositiveDouble private constructor(val value: Double) : Comparable<PositiveDouble> {

  /**
   * Adds two [PositiveDouble] instances and returns a new [Option] of PositiveDouble.
   * @return [Some] if the resulting value is positive and [Double.isFinite], otherwise [None].
   *
   * ### Examples:
   * ```
   * PositiveDouble(2.0) + PositiveDouble(3.0) // Some(PositiveDouble(5.0))
   *
   * // Edge cases
   * PositiveDouble(Double.MAX_VALUE) + one // Some(PositiveDouble(Double.MAX_VALUE))
   * PositiveDouble(Double.MAX_VALUE) + PositiveDouble(1_000_000_000.0) // Some(PositiveDouble(Double.MAX_VALUE))
   * PositiveDouble(Double.MAX_VALUE) + PositiveDouble(Double.MAX_VALUE) // None
   * ```
   */
  operator fun plus(other: PositiveDouble): Option<PositiveDouble> =
    fromDouble(value + other.value)

  /**
   * Subtracts one [PositiveDouble] from another and returns a new [Option] of [PositiveDouble].
   * @return [Some] if the resulting value is positive and [Double.isFinite], otherwise [None].
   *
   * ### Examples:
   * ```
   * PositiveDouble(5.0) - PositiveDouble(3.0) // Some(PositiveDouble(2.0))
   * PositiveDouble(2.0) - PositiveDouble(3.0) // None
   * ```
   */
  operator fun minus(other: PositiveDouble): Option<PositiveDouble> =
    fromDouble(value - other.value)

  /**
   * Multiplies two [PositiveDouble] instances and returns a new [Option] of [PositiveDouble].
   * @return [Some] if the resulting value is positive and [Double.isFinite], otherwise [None].
   *
   * ### Examples:
   * ```
   * PositiveDouble(2.0) * PositiveDouble(3.0) // Some(PositiveDouble(6.0))
   *
   * // Edge cases
   * PositiveDouble(Double.MAX_VALUE) * PositiveDouble(0.5) // Some(PositiveDouble(Double.MAX_VALUE / 2))
   * PositiveDouble(Double.MAX_VALUE) * PositiveDouble(2.0) // None
   * ```
   */
  operator fun times(other: PositiveDouble): Option<PositiveDouble> =
    fromDouble(value * other.value)

  /**
   * Divides one [PositiveDouble] by another and returns a new [Option] of [PositiveDouble].
   * If the divisor is zero, returns [None].
   * @return [Some] if the resulting value is positive and [Double.isFinite], otherwise [None].
   *
   * ### Examples:
   * ```
   * PositiveDouble(6.0) / PositiveDouble(2.0) // Some(PositiveDouble(3.0))
   *
   * // Edge cases
   * PositiveDouble(6.0) / PositiveDouble(0.0) // None
   * ```
   */
  operator fun div(other: PositiveDouble): Option<PositiveDouble> =
    if (other.value != 0.0) fromDouble(value / other.value) else None

  /**
   * Computes the remainder of dividing one [PositiveDouble] by another and returns a new [Option] of [PositiveDouble].
   * If the divisor is zero, returns [None].
   * @return [Some] if the resulting value is positive and [Double.isFinite], otherwise [None].
   *
   * ### Examples:
   * ```
   * PositiveDouble(7.0) % PositiveDouble(3.0) // Some(PositiveDouble(1.0))
   *
   * // Edge cases
   * PositiveDouble(7.0) % PositiveDouble(0.0) // None
   * ```
   */
  operator fun rem(other: PositiveDouble): Option<PositiveDouble> =
    if (other.value != 0.0) fromDouble(value % other.value) else None

  override fun compareTo(other: PositiveDouble): Int = value.compareTo(other.value)

  companion object {
    /**
     * Safely creates a [PositiveDouble] from a [Double] value, returning an [Option] of [PositiveDouble].
     * If the input value is not positive or not finite, returns [None].
     *
     * @param value The [Double] value to be converted into a [PositiveDouble].
     * @return [Some] [PositiveDouble] for positive and finite [Double] values, otherwise [None].
     */
    fun fromDouble(value: Double): Option<PositiveDouble> =
      if (value > 0.0 && value.isFinite()) Some(PositiveDouble(value)) else None

    /**
     * Creates a [PositiveDouble] from a [Double] value in an unsafe manner.
     *
     * ## Dangerous operation
     * This function should be used with caution as it may throw an exception for invalid input.
     *
     * @param value A [Double] value that must be positive and finite.
     * @param tag An optional [String] used to better localize the failure in case of [ArrowExactPositiveDoubleConstraintError].
     * @return [PositiveDouble] if the provided [Double] value is positive and finite, otherwise, throws an exception.
     * @throws [ArrowExactPositiveDoubleConstraintError] when the provided [value] isn't positive and finite.
     */
    fun unsafe(
      value: Double,
      tag: String = "PositiveDouble#unsafe"
    ): PositiveDouble = fromDouble(value).fold(
      ifEmpty = {
        throw ArrowExactPositiveDoubleConstraintError(
          violatingValue = value,
          tag = tag,
        )
      },
      ifSome = { it }
    )

    /**
     * Creates a [PositiveDouble] from a [Double] value using the [unsafe] method.
     *
     * @param value A [Double] value that must be positive and finite.
     * @return [PositiveDouble] if the provided [Double] value is positive and finite, otherwise, throws an exception.
     * @throws [ArrowExactPositiveDoubleConstraintError] when the provided [value] isn't positive and finite.
     */
    operator fun invoke(value: Double): PositiveDouble = unsafe(value, tag = "PositiveDouble()")
  }

}

/**
 * Safely applies the provided function [f] to transform the underlying [PositiveDouble.value].
 *
 * The transformation function [f] is used to map the current [PositiveDouble.value] to another [Double].
 * If the result is both finite and positive, a [Some] containing the new [PositiveDouble] is returned.
 * If the result is not finite or positive, [None] is returned.
 *
 * @param f A transformation function that maps the wrapped [PositiveDouble.value] to another [Double].
 * @return [Some] containing a new [PositiveDouble] if the result of applying [f] is positive and finite, otherwise [None].
 *
 * ### Example:
 * ```
 * val positive = PositiveDouble(2.0)
 * val result = positive.mapSafe { it * 3 } // Some(PositiveDouble(6.0))
 * val negativeResult = positive.mapSafe { -it } // None
 * ```
 */
fun PositiveDouble.mapSafe(f: (Double) -> Double): Option<PositiveDouble> = fromDouble(f(value))


/**
 * Represents an error that occurs when a value violates the constraint of being both positive and finite.
 *
 * This error is thrown when a value that does not meet the requirements of a [PositiveDouble] is passed
 * to an unsafe API, such as [PositiveDouble.unsafe] or [PositiveDouble.invoke].
 *
 * @param violatingValue The [Double] value that violates the positive and finite constraint.
 * @param tag An optional [String] that provides additional context or information about the source of the error.
 *
 * ### Example:
 * ```
 * try {
 *     val invalidPositiveDouble = PositiveDouble(-1.0)
 * } catch (e: ArrowExactPositiveDoubleConstraintError) {
 *     println(e.message) // "Value '-1.0' does not meet the constraint 'PositiveDouble', which requires a positive and finite number."
 * }
 * ```
 */
class ArrowExactPositiveDoubleConstraintError(
  violatingValue: Double,
  tag: String?,
) : ArrowExactConstraintViolationError(
  constraint = "PositiveDouble",
  requirement = "a positive and finite number",
  violatingValue = violatingValue,
  tag = tag
)
