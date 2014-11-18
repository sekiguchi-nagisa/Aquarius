package aquarius;

import aquarius.Parser.PatternWrapper;
import aquarius.expression.ParsingExpression;

public class Rule<R> extends ParsingExpression<R> {
	/**
	 * for memoization
	 */
	private final int ruleIndex;

	private final boolean returnable;
	/**
	 * will be null after call initExpr()
	 */
	private PatternWrapper<R> wrapper;

	private ParsingExpression<R> pattern;

	public Rule(int ruleIndex, PatternWrapper<R> wrapper, boolean returnable) {
		this.ruleIndex = ruleIndex;
		this.wrapper = wrapper;
		this.returnable = returnable;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean parse(ParserContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isReturnable() {
		return this.returnable;
	}

	void initExpr() {
		this.pattern = this.wrapper.invoke();
		this.wrapper = null;
	}

	public int getRuleIndex() {
		return this.ruleIndex;
	}

	public ParsingExpression<R> getPattern() {
		return this.pattern;
	}
}
