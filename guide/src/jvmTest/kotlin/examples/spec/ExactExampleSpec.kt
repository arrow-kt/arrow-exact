// This file was automatically generated from Exact.kt by Knit tool. Do not edit.
package arrow.exact.knit.test

import io.kotest.core.spec.style.StringSpec
import kotlinx.knit.test.captureOutput
import kotlinx.knit.test.verifyOutputLines

class ExactExampleSpec : StringSpec({
  "ExampleExact01" {
    captureOutput("ExampleExact01") { arrow.exact.knit.example.exampleExact01.example() }
      .verifyOutputLines(
        "Either.Right(NotBlankString(value=Hello))",
        "Either.Left(ExactError(message=Failed condition.))"
      )
  }

}) {
  override fun timeout(): Long = 1000
}
