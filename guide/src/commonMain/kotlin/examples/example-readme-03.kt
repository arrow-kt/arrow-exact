// This file was automatically generated from README.md by Knit tool. Do not edit.
package arrow.exact.knit.example.exampleReadme03

import arrow.core.raise.ensure
import arrow.exact.Exact
import arrow.exact.ExactError

@JvmInline
value class NotBlankString private constructor(val value: String) {
   companion object : Exact<String, NotBlankString> by Exact({
     ensure(it.isNotBlank()) { ExactError("Cannot be blank.") }
     NotBlankString(it)
   })
}
