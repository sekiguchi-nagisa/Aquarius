package aquarius;

import aquarius.expression.ParsingExpression;

public interface Parser {
	public default <R> Rule<R> rule(PatternWrapper<R> expr) {
		throw new RuntimeException("forbidden invokation: rule");
	}

	public default Rule<Void> ruleVoid(PatternWrapper<Void> expr) {
		throw new RuntimeException("forbidden invokation: ruleVoid");
	}

	@FunctionalInterface
	public static interface PatternWrapper<R> {
		public ParsingExpression<R> invoke();
	}
}
