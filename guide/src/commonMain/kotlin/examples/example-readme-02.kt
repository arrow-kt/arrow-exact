// This file was automatically generated from README.md by Knit tool. Do not edit.
package arrow.exact.knit.example.exampleReadme02

import arrow.exact.Exact
import arrow.exact.ensure
import kotlin.jvm.JvmInline

@JvmInline
value class NotBlankString private constructor(val value: String) {
   companion object : Exact<String, NotBlankString> by Exact({
     ensure(it.isNotBlank())
     NotBlankString(it)
   })
}
