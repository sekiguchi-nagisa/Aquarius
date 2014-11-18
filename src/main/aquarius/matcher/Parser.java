package aquarius.matcher;

import aquarius.matcher.expression.ParsingExpression;

public interface Parser {
	public default <R> NoneTerminal<R> rule(Pattern<R> expr) {
		throw new RuntimeException("unimplemented method: rule");
	}

	@FunctionalInterface
	public static interface Pattern<R> {
		public ParsingExpression<R> invoke();
	}
}
