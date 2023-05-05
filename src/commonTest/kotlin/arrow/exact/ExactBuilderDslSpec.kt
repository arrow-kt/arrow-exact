package arrow.exact

import arrow.core.raise.ensureNotNull
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.data.row
import io.kotest.datatest.withData
import io.kotest.matchers.booleans.shouldBeTrue
import kotlin.jvm.JvmInline

sealed interface Artificial {
  object Admin : Artificial
  data class User(val id: Int) : Artificial
}

@JvmInline
value class ArtificialExample private constructor(val value: Artificial) {
  companion object : Exact<String, ArtificialExample> by exactBuilder({ builder ->
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
      .build(::ArtificialExample)
  })
}

class ExactBuilderDslSpec : FreeSpec({
  "artificial user" {
    val res = ArtificialExample.from("u/123")

    res shouldBeRight Artificial.User(123)
  }

  "artificial admin" {
    val res = ArtificialExample.from("a/")

    res shouldBeRight Artificial.Admin
  }

  "invalid" - {
    withData(
      row(""),
      row(" "),
      row("Okay"),
      row("u/Fail"),
    ) { (string) ->
      val res = ArtificialExample.from(string)

      res.isLeft().shouldBeTrue()
    }
  }
})
