package aquarius.combinator;

import aquarius.combinator.expression.Rule;

public abstract class Grammar {
	private int ruleIndexCount = 0;

	protected <R> Rule<R> rule(String ruleName) {
		return new Rule<>(ruleName, this.ruleIndexCount++);
	}

	public int getIndexSize() {
		return this.ruleIndexCount;
	}
}
