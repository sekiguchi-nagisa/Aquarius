package aquarius.matcher;

@FunctionalInterface
public interface ParsingActionNoArg<R> {
	public R invoke(ParserContext context) throws FailedActionException, Exception;
}
