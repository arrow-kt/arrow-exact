data class ArrowExactConstraintError(
  private val constraint: String,
  private val violatingValue: Any,
  private val tag: String?,
  private val requirement: String,
) : IllegalArgumentException(errorMessage(constraint, violatingValue, tag, requirement))

private fun errorMessage(
  constraint: String,
  violatingValue: Any,
  tag: String?,
  requirement: String,
): String = buildString {
  append("ArrowExact $constraint constraint violated")
  tag?.let {
    append(" in \"$it\" call")
  }
  append(": $violatingValue must be $requirement.")
}


fun main() {
  throw ArrowExactConstraintError(
    constraint = "PositiveDouble",
    violatingValue = -3.5,
    tag = "main test",
    requirement = "must be positive and finite."
  )
}
