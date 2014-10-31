package aquarius.matcher;

@FunctionalInterface
public interface ParsingAction<A, R> {
	public R invoke(ParserContext context, A arg) throws FailedActionException, Exception;
}
