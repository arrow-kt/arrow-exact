/**
 * Represents a base error that occurs when a value violates a given constraint.
 *
 * This error is thrown when a value does not meet the requirements of a specific constraint, such as the
 * constraint of being both positive and finite in the case of [PositiveDouble].
 *
 * @param constraint A [String] describing the violated constraint, such as "PositiveDouble".
 * @param requirement A [String] describing the requirement that the violating value failed to meet, such as "a positive and finite number".
 * @param violatingValue The value that violates the specified constraint.
 * @param tag An optional [String] that provides additional context or information about the source of the error.
 *
 * ### Example:
 * ```
 * open class MyCustomConstraintError(
 *   violatingValue: Double,
 *   tag: String?,
 * ) : ArrowExactConstraintViolationError(
 *   constraint = "MyCustomConstraint",
 *   requirement = "a custom constraint requirement",
 *   violatingValue = violatingValue,
 *   tag = tag
 * )
 *
 * try {
 *     // code that may throw a MyCustomConstraintError
 * } catch (e: MyCustomConstraintError) {
 *     println(e.message) // "Value 'some_value' does not meet the constraint 'MyCustomConstraint', which requires a custom constraint requirement."
 * }
 * ```
 */
open class ArrowExactConstraintViolationError(
  val constraint: String,
  val requirement: String,
  val violatingValue: Any,
  val tag: String?,
) : IllegalArgumentException(errorMessage(constraint, violatingValue, tag, requirement))

private fun errorMessage(
  constraint: String,
  violatingValue: Any,
  tag: String?,
  requirement: String,
): String = buildString {
  append("[ArrowExact] $constraint constraint violated")
  tag?.let {
    append(" in \"$it\"")
  }
  append(". The value \"$violatingValue\" doesn't meet the requirement: \"Must be $requirement.\"")
}


fun main() {
  throw ArrowExactConstraintViolationError(
    constraint = "PositiveDouble",
    violatingValue = -3.5,
    tag = "main test",
    requirement = "must be positive and finite."
  )
}
