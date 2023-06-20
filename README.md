Module Arrow Exact

Arrow Exact allows you to use Kotlin's type system to enforce exactness of data structures.

## Introduction

<!--- TEST_NAME ReadMeSpec -->

Exact allows automatically projecting smart-constructors on a `Companion Object`. We can for
example easily create a `NotBlankString` type that is a `String` that is not blank, leveraging
the Arrow's `Raise` DSL to `ensure` the value is not blank.

```kotlin
import arrow.core.raise.Raise
import arrow.exact.Exact
import arrow.exact.ErrorMessage
import arrow.exact.ensure
import kotlin.jvm.JvmInline

@JvmInline
value class NotBlankString private constructor(val value: String) { 
  companion object : Exact<String, NotBlankString> {
    override fun Raise<ErrorMessage>.spec(raw: String): NotBlankString { 
      ensure(raw.isNotBlank())
      return NotBlankString(raw)
    }
  }
}
```

We can then easily create values of `NotBlankString` `from` a `String`, which returns us a
`Either` with the `ErrorMessage` or the `NotBlankString`. We can also use `fromOrNull` to get a
nullable value, or `fromOrThrow` to throw an `ExactException`.

**note:** Make sure to define your constructor as `private` to prevent creating invalid values.

```kotlin
fun example() {
  println(NotBlankString.from("Hello"))
  println(NotBlankString.from(""))
}
```

The output of the above program is:

```text
Either.Right(NotBlankString(value=Hello))
Either.Left(ErrorMessage(message=Failed condition.))
```

<!--- KNIT example-readme-01.kt -->
<!--- TEST -->

You can also define `Exact` by using Kotlin delegation.
<!--- INCLUDE
import arrow.exact.Exact
import arrow.exact.ensure
import kotlin.jvm.JvmInline
-->
```kotlin
@JvmInline
value class NotBlankString private constructor(val value: String) {
   companion object : Exact<String, NotBlankString> by Exact({
     ensure(it.isNotBlank())
     NotBlankString(it)
   })
}
```
<!--- KNIT example-readme-02.kt -->

You can define a second type `NotBlankTrimmedString` that is a `NotBlankString` that is also
trimmed. `ensureExact` allows us to compose `Exact` instances and easily
reuse the `NotBlankString` type.
<!--- INCLUDE
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
-->

```kotlin
@JvmInline
value class NotBlankTrimmedString private constructor(val value: String) { 
  companion object : Exact<String, NotBlankTrimmedString> by Exact({ 
    ensure(it, NotBlankString)
    NotBlankTrimmedString(it.trim())
  })
}
```

<!--- KNIT example-readme-03.kt -->
