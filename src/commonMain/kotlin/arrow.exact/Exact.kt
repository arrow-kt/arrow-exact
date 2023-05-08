package arrow.exact

import arrow.core.Either

public interface Exact<A, out R> : ExactEither<ExactError, A, R>

public data class ExactError(val message: String)

public interface ExactEither<out E : Any, A, out R> {

  public fun from(value: A): Either<E, R>

  public fun fromOrNull(value: A): R? = from(value).getOrNull()

  public fun fromOrThrow(value: A): R =
    when (val result = from(value)) {
      is Either.Left -> throw ExactException(result.value)
      is Either.Right -> result.value
    }
}

public class ExactException(error: Any) : IllegalArgumentException("ArrowExact error: $error")
