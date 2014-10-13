package aquarius.combinator;

import aquarius.runtime.Result;

@FunctionalInterface
public interface ParsingAction<A, R> {
	public Result<R> invoke(A arg);
}
