package examples

import arrow.core.Either
import arrow.exact.And
import arrow.exact.BlankInstance.Companion.stringInstance
import arrow.exact.ExactError
import arrow.exact.ExactValue
import arrow.exact.ExactValueSpec
import arrow.exact.NotBlank
import arrow.exact.Trimmed

typealias NotBlankString = ExactValue<String, NotBlank<String>>
typealias TrimmedString = ExactValue<String, Trimmed>
typealias NotBlankTrimmedString = ExactValue<String, NotBlankTrimmed>

object NotBlankTrimmed : ExactValueSpec<String> by And(NotBlank(stringInstance), Trimmed)

fun String.toNotBlankString(): Either<ExactError, NotBlankString> =
    ExactValue.fromSpec(this, NotBlank(stringInstance))

fun String.toTrimmedString(): Either<ExactError, TrimmedString> =
    ExactValue.fromSpec(this, Trimmed)

fun String.toNotBlankTrimmedString(): Either<ExactError, NotBlankTrimmedString> =
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

