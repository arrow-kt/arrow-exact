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
