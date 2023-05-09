Module Arrow Exact

Arrow Exact allows you to use Kotlin's type system to enforce exactness of data structures.

## Introduction

<!--- TEST_NAME ReadMeSpec -->

Exact allows automatically projecting smart-constructors on a `Companion Object`. We can for
example easily create a `NotBlankString` type that is a `String` that is not blank, leveraging
the Arrow's `Raise` DSL to `ensure` the value is not blank.

```kotlin
import arrow.core.raise.ensure
import arrow.exact.Exact
import arrow.exact.ExactError
import arrow.exact.exact

@JvmInline
value class NotBlankString private constructor(val value: String) {
  companion object : Exact<String, NotBlankString> by exact({ raw ->
    ensure(raw.isNotBlank()) { ExactError("Cannot be blank.") }
    NotBlankString(raw)
  })
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
Either.Left(ExactError(message=Cannot be blank.))
```

<!--- KNIT example-readme-01.kt -->
<!--- TEST -->

You can define a second type `NotBlankTrimmedString` that is a `NotBlankString` that is also
trimmed. Since the `exact` constructor allows us to compose `Exact` instances, we can easily
reuse the `NotBlankString` type.
<!--- INCLUDE
import arrow.core.raise.ensure
import arrow.exact.Exact
import arrow.exact.ExactError
import arrow.exact.exact

@JvmInline value class NotBlankString private constructor(val value: String) {
  companion object : Exact<String, NotBlankString> by exact({ raw ->
    ensure(raw.isNotBlank()) { ExactError("Cannot be blank.") }
    NotBlankString(raw)
  })
}
-->

```kotlin
@JvmInline
value class NotBlankTrimmedString private constructor(val value: String) {
  companion object : Exact<String, NotBlankTrimmedString> by exact({ raw ->
    val notBlank = NotBlankString.from(raw).bind()
    NotBlankTrimmedString(notBlank.value.trim())
  })
}
```

<!--- KNIT example-readme-02.kt -->
