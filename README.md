# Arrow Exact

Arrow Exact allows you to use Kotlin's type system to enforce **exactness of data structures.**
If you're practicing [Domain-driven Design (DDD)](https://en.wikipedia.org/wiki/Domain-driven_design) Arrow Exact will be
your trusted companion and help you easily create your own constrained (refined) types for example like `NotBlankString`, `PositiveInt`, `OrderId` 
that explicitly describe your domain model.

See this article on [functional domain modeling](https://arrow-kt.io/learn/design/domain-modeling/) to learn more.

## Quickstart

[![Latest Version](https://img.shields.io/github/v/release/arrow-kt/arrow-exact)](https://github.com/arrow-kt/arrow-exact/releases/latest)

It's easy! Just put this in your module's `build.gradle` file.

**build.gradle.kts** _(Gradle Kotlin DSL)_
```kotlin
implementation("io.arrow-kt:arrow-exact-jvm:0.1.0")
```

**build.gradle** _(Gradle Groovy)_
```groovy
implementation 'io.arrow-kt:arrow-exact-jvm:0.1.0'
```

## Introduction

<!--- TEST_NAME ReadMeSpec -->

Exact allows automatically projecting smart-constructors on a `companion object`. We can for
example easily create a `NotBlankString` type that is a `String` that is not blank, leveraging
the Arrow's `Raise` DSL to `ensure` the value is not blank.

```kotlin
import arrow.core.raise.Raise
import arrow.exact.Exact
import arrow.exact.ExactError
import arrow.exact.ensure
import kotlin.jvm.JvmInline

@JvmInline
value class NotBlankString private constructor(val value: String) { 
  companion object : Exact<String, NotBlankString> {
    override fun Raise<ExactError>.spec(raw: String): NotBlankString { 
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
Either.Left(ExactError(message=Failed condition.))
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
import arrow.core.raise.Raise
import arrow.exact.Exact
import arrow.exact.ExactError
import arrow.exact.ensure
import kotlin.jvm.JvmInline

@JvmInline
value class NotBlankString private constructor(val value: String) {
  companion object : Exact<String, NotBlankString> {
    override fun Raise<ExactError>.spec(raw: String): NotBlankString {
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
    override fun Raise<ExactError>.spec(raw: String): NotBlankTrimmedString { 
      ensure(raw, NotBlankString)
      return NotBlankTrimmedString(raw.trim())
    }
  }
}
```

<!--- KNIT example-readme-03.kt -->
