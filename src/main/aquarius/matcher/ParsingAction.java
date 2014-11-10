package aquarius.matcher;

@FunctionalInterface
public interface ParsingAction<R, A> {
	public R invoke(ParserContext context, A arg) throws FailedActionException, Exception;
}
