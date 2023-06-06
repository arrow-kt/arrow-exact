// This file was automatically generated from README.md by Knit tool. Do not edit.
package arrow.exact.knit.test

import io.kotest.core.spec.style.StringSpec
import kotlinx.knit.test.captureOutput
import kotlinx.knit.test.verifyOutputLines

class ReadMeSpec : StringSpec({
  "ExampleReadme01" {
    captureOutput("ExampleReadme01") { arrow.exact.knit.example.exampleReadme01.example() }
      .verifyOutputLines(
        "Either.Right(NotBlankString(value=Hello))",
        "Either.Left(Failed condition.)"
      )
  }

  "ExampleReadme02" {
    captureOutput("ExampleReadme02") { arrow.exact.knit.example.exampleReadme02.example() }
      .verifyOutputLines(
        "Either.Left(String must not be blank.)"
      )
  }

}) {
  override fun timeout(): Long = 1000
}
