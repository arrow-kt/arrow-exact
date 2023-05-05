package arrow.exact

import arrow.core.Either
import arrow.core.raise.ensureNotNull
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.failure
import io.kotest.core.spec.style.FreeSpec
import io.kotest.data.row
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import kotlin.jvm.JvmInline

sealed interface Artificial {
  object Admin : Artificial
  data class User(val id: Int) : Artificial
}

@JvmInline
value class ArtificialConstraintType private constructor(val value: Artificial) {
  companion object : Exact<String, ArtificialConstraintType> by exactBuilder({ builder ->
    builder.mustBe(String::isNotBlank)
      .transform(String::trim)
      .transformOrRaise {
        when {
          it.startsWith("a/") -> Artificial.Admin
          it.startsWith("u/") -> {
            val userId = it.drop(2).toIntOrNull()
            ensureNotNull(userId) { ExactError("Invalid user id in: $it") }
            Artificial.User(userId)
          }

          else -> raise(ExactError("Unknown value: $it"))
        }
      }
      .build(::ArtificialConstraintType)
  })
}

class ExactBuilderDslSpec : FreeSpec({
  "artificial user" {
    when (val res = ArtificialConstraintType.from("u/123")) {
      is Either.Left -> failure(res.value.message)
      is Either.Right -> res.value.value shouldBe Artificial.User(123)
    }
  }

  "artificial admin" {
    when (val res = ArtificialConstraintType.from("a/")) {
      is Either.Left -> failure(res.value.message)
      is Either.Right -> res.value.value shouldBe Artificial.Admin
    }
  }

  "invalid" - {
    withData(
      row(""),
      row(" "),
      row("Okay"),
      row("u/Fail"),
    ) { (string) ->
      val res = ArtificialConstraintType.from(string)

      res.shouldBeLeft()
    }
  }
})
