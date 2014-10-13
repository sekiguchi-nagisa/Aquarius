package aquarius.combinator.expression;

import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.ParserContext;
import aquarius.runtime.Result;

/**
* try to match rule. return matched result
* -> rule
* @author skgchxngsxyz-opensuse
 * @param <R>
*
*/
public class Rule<R> implements ParsingExpression<R> {
	private final String ruleName;
	private final int ruleIndex;
	private ParsingExpression<R> pattern;

	public Rule(String ruleName, int ruleIndex) {
		this.ruleName = ruleName;
		this.ruleIndex = ruleIndex;
	}

	public String getRuleName() {
		return this.ruleName;
	}

	public void setPattern(ParsingExpression<R> pattern) {
		this.pattern = pattern;
	}

	public ParsingExpression<R> getPattern() {
		return this.pattern;
	}

	public int getRuleIndex() {
		return this.ruleIndex;
	}

	public Result<R> parse(ParserContext context) {
		return context.dispatchRule(this);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitRule(this);
	}
}