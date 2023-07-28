package arrow.exact

import arrow.core.Either
import arrow.core.raise.Raise
import arrow.core.raise.either

//TODO: add documentation
//TODO: fix coding style (see #33)

public typealias SuspendExact<A, R> = SuspendExactEither<ExactError, A, R>

public interface SuspendExactEither<out Error: Any, in Input, out Output> {

	public suspend fun Raise<Error>.spec(raw: Input): Output

	public suspend fun from(value: Input): Either<Error, Output> = either { spec(value) }

	public suspend fun fromOrNull(value: Input): Output? = from(value).getOrNull()

	public suspend fun fromOrThrow(value: Input): Output =
		when (val result = from(value)) {
			is Either.Left -> throw ExactException(result.value)
			is Either.Right -> result.value
		}
}
