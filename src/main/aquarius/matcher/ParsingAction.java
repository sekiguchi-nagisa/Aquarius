package aquarius.matcher;

public interface ParsingAction<R, A> {
	@FunctionalInterface
	public static interface ParsingActionReturn<R, A> extends ParsingAction<R, A> {
		public R invoke(ParserContext context, A arg) throws FailedActionException, Exception;
	}

	public static interface ParsingActionNoReturn<A> extends ParsingAction<Void, A> {
		public void invoke(ParserContext context, A arg) throws FailedActionException, Exception;
	}
}
