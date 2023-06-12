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
        "Either.Left(Failed condition.)"
      )
  }

  "ExampleExact02" {
    captureOutput("ExampleExact02") { arrow.exact.knit.example.exampleExact02.example() }
      .verifyOutputLines(
        "Either.Left(String must not be blank.)"
      )
  }

}) {
  override fun timeout(): Long = 1000
}
