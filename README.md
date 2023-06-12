Module Arrow Exact

Arrow Exact allows you to use Kotlin's type system to enforce exactness of data structures.

## Introduction

<!--- TEST_NAME ReadMeSpec -->

Exact allows automatically projecting smart-constructors on a `Companion Object`. We can for
example easily create a `NotBlankString` type that is a `String` that is not blank, leveraging
the Arrow's `Raise` DSL to `ensure` the value is not blank.

<!--- INCLUDE
import arrow.core.raise.Raise
import arrow.exact.Exact
import arrow.exact.ensure
import kotlin.jvm.JvmInline
-->
```kotlin
@JvmInline
value class NotBlankString private constructor(val value: String) { 
  companion object : Exact<String, NotBlankString> {
    override fun Raise<String>.spec(raw: String): NotBlankString { 
      ensure(raw.isNotBlank())
      return NotBlankString(raw)
    }
  }
}
```

We can then easily create values of `NotBlankString` `from` a `String`, which returns us a
`Either` with the `ExactError` or the `NotBlankString`. We can also use `fromOrNull` to get a
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
Either.Left(Failed condition.)
```

<!--- KNIT example-readme-01.kt -->
<!--- TEST -->

By default, if no error message is provided it'll use `Failed condition.`,
but just like `require` from the Kotlin Standard Library you can provide your own error message.

<!--- INCLUDE
import arrow.core.raise.Raise
import arrow.exact.Exact
import arrow.core.raise.ensure
import kotlin.jvm.JvmInline
-->
```kotlin
@JvmInline
value class NotBlankString private constructor(val value: String) { 
  companion object : Exact<String, NotBlankString> {
    override fun Raise<String>.spec(raw: String): NotBlankString { 
      ensure(raw.isNotBlank()) { "String must not be blank." }
      return NotBlankString(raw)
    }
  }
}

fun example() {
  println(NotBlankString.from(""))
}
```

The output of the above program is:

```text
Either.Left(String must not be blank.)
```

<!--- KNIT example-readme-02.kt -->
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
<!--- KNIT example-readme-03.kt -->

You can define a second type `NotBlankTrimmedString` that is a `NotBlankString` that is also
trimmed. `ensureExact` allows us to compose `Exact` instances and easily
reuse the `NotBlankString` type.
<!--- INCLUDE
import arrow.core.raise.Raise
import arrow.exact.Exact
import arrow.exact.ensure
import kotlin.jvm.JvmInline

@JvmInline
value class NotBlankString private constructor(val value: String) {
  companion object : Exact<String, NotBlankString> {
    override fun Raise<String>.spec(raw: String): NotBlankString {
      ensure(raw.isNotBlank())
      return NotBlankString(raw)
    }
  }
}
-->

```kotlin
@JvmInline
value class NotBlankTrimmedString private constructor(val value: String) { 
  companion object : Exact<String, NotBlankTrimmedString> { 
    override fun Raise<String>.spec(raw: String): NotBlankTrimmedString { 
      ensure(raw, NotBlankString)
      return NotBlankTrimmedString(raw.trim())
    }
  }
}
```

<!--- KNIT example-readme-04.kt -->
