package number

import ArrowExactConstraintError
import io.kotest.core.spec.style.FreeSpec

class PositiveDoubleTest : FreeSpec({
  "throws exception" {
    throw ArrowExactConstraintError(
      constraint = "PositiveDouble",
      violatingValue = -3.5,
      tag = "main test",
      requirement = "must be positive and finite."
    )
  }
})
