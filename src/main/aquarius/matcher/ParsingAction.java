package aquarius.matcher;

import aquarius.runtime.Result;

@FunctionalInterface
public interface ParsingAction<A, R> {
	public Result<R> invoke(ParserContext context, A arg) throws FailedActionException, Exception;
}
