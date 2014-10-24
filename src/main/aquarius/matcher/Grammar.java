package aquarius.matcher;

import java.util.HashMap;
import java.util.Map;

import aquarius.matcher.expression.ParsingExpression;
import aquarius.runtime.Result;

public abstract class Grammar {
	private int ruleIndexCount = 0;
	private final Map<String, Rule<?>> ruleMap = new HashMap<>();

	protected <R> Rule<R> rule(String ruleName) {
		return this.rule(ruleName, null);
	}

	protected <R> Rule<R> rule(String ruleName, ParsingExpression<R> pattern) {
		if(this.ruleMap.containsKey(ruleName)) {
			throw new RuntimeException("already defined rule name: " + ruleName);
		}
		Rule<R> rule = new Rule<>(ruleName, this.ruleIndexCount++, pattern);
		this.ruleMap.put(ruleName, rule);
		return rule;
	}

	protected <R> void def(Rule<R> rule, ParsingExpression<R> pattern) {
		rule.pattern = pattern;
	}

	public int getIndexSize() {
		return this.ruleIndexCount;
	}

	public <R> Rule<R> getRule(String ruleName) {
		@SuppressWarnings("unchecked")
		Rule<R> rule = (Rule<R>) this.ruleMap.get(ruleName);
		if(rule == null) {
			throw new IllegalArgumentException("undefined rule: " + ruleName);
		}
		return rule;
	}

	/**
	* try to match rule. return matched result
	* -> rule
	* @author skgchxngsxyz-opensuse
	 * @param <R>
	*
	*/
	public static class Rule<R> implements ParsingExpression<R> {
		private final String ruleName;
		private final int ruleIndex;
		private ParsingExpression<R> pattern;

		private Rule(String ruleName, int ruleIndex) {
			this(ruleName, ruleIndex, null);
		}

		private Rule(String ruleName, int ruleIndex, ParsingExpression<R> pattern) {
			this.ruleName = ruleName;
			this.ruleIndex = ruleIndex;
			this.pattern = pattern;
		}

		public String getRuleName() {
			return this.ruleName;
		}

		public ParsingExpression<R> getPattern() {
			return this.pattern;
		}

		public int getRuleIndex() {
			return this.ruleIndex;
		}

		@Override
		public String toString() {
			return this.getRuleName();
		}

		@Override	// auto-gen
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((pattern == null) ? 0 : pattern.hashCode());
			result = prime * result + ruleIndex;
			result = prime * result
					+ ((ruleName == null) ? 0 : ruleName.hashCode());
			return result;
		}

		@Override	//auto-gen
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Rule<?> other = (Rule<?>) obj;
			if (pattern == null) {
				if (other.pattern != null)
					return false;
			} else if (!pattern.equals(other.pattern))
				return false;
			if (ruleIndex != other.ruleIndex)
				return false;
			if (ruleName == null) {
				if (other.ruleName != null)
					return false;
			} else if (!ruleName.equals(other.ruleName))
				return false;
			return true;
		}

		public Result<R> parse(ParserContext context) {
			return context.dispatchRule(this);
		}

		@Override
		public <T> T accept(ExpressionVisitor<T> visitor) {
			return visitor.visitRule(this);
		}
	}
}
