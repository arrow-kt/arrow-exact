package number

import ArrowExactConstraintViolationError
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import number.PositiveDouble.Companion.fromDouble
import number.PositiveDouble.Companion.invoke
import kotlin.jvm.JvmInline

/**
 * Represents a positive and finite [Double] value.
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
   */
  operator fun minus(other: PositiveDouble): Option<PositiveDouble> =
    fromDouble(value - other.value)

  /**
   * Multiplies two [PositiveDouble] instances and returns a new [Option] of [PositiveDouble].
   */
  operator fun times(other: PositiveDouble): Option<PositiveDouble> =
    fromDouble(value * other.value)

  /**
   * Divides one [PositiveDouble] by another and returns a new [Option] of [PositiveDouble].
   * If the divisor is zero, returns [None].
   */
  operator fun div(other: PositiveDouble): Option<PositiveDouble> =
    if (other.value != 0.0) fromDouble(value / other.value) else None

  /**
   * Computes the remainder of dividing one [PositiveDouble] by another and returns a new [Option] of [PositiveDouble].
   * If the divisor is zero, returns [None].
   */
  operator fun rem(other: PositiveDouble): Option<PositiveDouble> =
    if (other.value != 0.0) fromDouble(value % other.value) else None

  override fun compareTo(other: PositiveDouble): Int = value.compareTo(other.value)

  companion object {
    /**
     * Safely creates a [PositiveDouble] from a [Double] value, returning an [Option] of [PositiveDouble].
     * If the input value is not positive or not finite, returns [None].
     */
    fun fromDouble(value: Double): Option<PositiveDouble> =
      if (value > 0.0 && value.isFinite()) Some(PositiveDouble(value)) else None

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

    operator fun invoke(value: Double): PositiveDouble = unsafe(value)
  }
}

fun PositiveDouble.mapSafe(f: (Double) -> Double): Option<PositiveDouble> =
  PositiveDouble.fromDouble(f(value))


class ArrowExactPositiveDoubleConstraintError(
  violatingValue: Double,
  tag: String?,
) : ArrowExactConstraintViolationError(
  constraint = "PositiveDouble",
  requirement = "a positive and finite number",
  violatingValue = violatingValue,
  tag = tag
)
