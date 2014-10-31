package aquarius.matcher;

@FunctionalInterface
public interface ParsingActionWithoutArg<R> {
	public R invoke(ParserContext context) throws FailedActionException, Exception;
}
