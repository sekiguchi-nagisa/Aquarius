package aquarius.action;

import aquarius.ParserContext;

public interface ParsingAction<R, A> {
	@FunctionalInterface
	public static interface Mapper<R, A> extends ParsingAction<R, A> {
		public R invoke(ParserContext context, A arg) throws FailedActionException, Exception;
	}

	public static interface Consumer<A> extends ParsingAction<Void, A> {
		public void invoke(ParserContext context, A arg) throws FailedActionException, Exception;
	}
}
