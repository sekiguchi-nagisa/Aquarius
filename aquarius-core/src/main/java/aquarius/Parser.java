package aquarius;

import aquarius.expression.ParsingExpression;

/**
 * if define new parse class, must be extends this interface.
 * @author skgchxngsxyz-opensuse
 *
 */
public interface Parser {
	/**
	 * create and set new parsing rule which constructing non null value. must not be override.
	 * @param expr
	 * right hand side expression of new parsing rule.
	 * @return
	 */
	public default <R> Rule<R> rule(PatternWrapper<R> expr) {
		throw new RuntimeException("forbidden invokation: rule");
	}

	/**
	 * create and set new parsing rule, must not be override.
	 * @param expr
	 * right hand side expression of new parsing rule.
	 * @return
	 */
	public default Rule<Void> ruleVoid(PatternWrapper<Void> expr) {
		throw new RuntimeException("forbidden invokation: ruleVoid");
	}

	@FunctionalInterface
	public static interface PatternWrapper<R> {
		public ParsingExpression<R> invoke();
	}
}
