package aquarius.matcher;

public interface ParsingActionNoArg<R> {
	@FunctionalInterface
	public static interface ParsingActionNoArgReturn<R> extends ParsingActionNoArg<R> {
		public R invoke(ParserContext context) throws FailedActionException, Exception;
	}

	@FunctionalInterface
	public static interface ParsingActionNoArgNoReturn extends ParsingActionNoArg<Void> {
		public void invoke(ParserContext context) throws FailedActionException, Exception;
	}
}
