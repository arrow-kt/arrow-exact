package examples.spec

import arrow.core.Either
import arrow.exact.ExactError
import arrow.exact.ExactValue
import arrow.exact.ExactValueSpec
import arrow.exact.operator.And
import arrow.exact.operator.BlankInstance
import arrow.exact.operator.NotBlank
import arrow.exact.operator.Trimmed
import io.kotest.core.spec.style.StringSpec
import kotlinx.knit.test.captureOutput
import kotlinx.knit.test.verifyOutputLines

class ExactValueSpecs : StringSpec({
    "ExactValue" {
        example()
        captureOutput("ExactValue") { example() }
            .verifyOutputLines(
                "Either.Right(ExactValue(value=Hello))",
                "Either.Left(ExactError(message=\"\" contains blank.))",
                "Either.Right(ExactValue(value=Hello))",
                "Either.Left(ExactError(message=\" \" contains whitespace.))",
                "Either.Right(ExactValue(value=Hello))",
                "Either.Left(ExactError(message=Failed to match Exact.))",
                "Either.Left(ExactError(message=Failed to match Exact.))"
            )
    }
}) {

    override fun timeout(): Long = 1000
}

typealias NotBlankString = ExactValue<String, ExactValueSpec<NotBlank<String>>>
typealias TrimmedString = ExactValue<String, ExactValueSpec<Trimmed>>
typealias NotBlankTrimmedString = ExactValue<String, ExactValueSpec<NotBlankTrimmed>>

object NotBlankTrimmed : ExactValueSpec<String> by And(NotBlank(BlankInstance.stringInstance), Trimmed)

fun String.toNotBlankString(): Either<ExactError, ExactValue<String, NotBlank<String>>> =
    ExactValue.fromSpec(this, NotBlank(BlankInstance.stringInstance))

fun String.toTrimmedString(): Either<ExactError, ExactValue<String, Trimmed>> =
    ExactValue.fromSpec(this, Trimmed)

fun String.toNotBlankTrimmedString(): Either<ExactError, ExactValue<String, NotBlankTrimmed>> =
    ExactValue.fromSpec(this, NotBlankTrimmed)

fun example() {
    println("Hello".toNotBlankString())
    println("".toNotBlankString())
    println("Hello".toTrimmedString())
    println(" ".toTrimmedString())
    println("Hello".toNotBlankTrimmedString())
    println("".toNotBlankTrimmedString())
    println(" ".toNotBlankTrimmedString())
}

