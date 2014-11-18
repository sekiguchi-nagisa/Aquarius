package aquarius;

import aquarius.expression.ParsingExpression;

public interface Parser {
	public default <R> Rule<R> rule(PatternWrapper<R> expr) {
		throw new RuntimeException("unimplemented method: rule");
	}

	public default Rule<Void> ruleVoid(PatternWrapper<Void> expr) {
		throw new RuntimeException("unimplemented method: rule");
	}

	@FunctionalInterface
	public static interface PatternWrapper<R> {
		public ParsingExpression<R> invoke();
	}
}
