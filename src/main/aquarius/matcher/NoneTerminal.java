package aquarius.matcher;

import aquarius.matcher.Parser.Pattern;
import aquarius.matcher.expression.ParsingExpression;

public class NoneTerminal<R> extends ParsingExpression<R> {
	private final int ruleIndex;
	private final Pattern<R> pattern;
	private ParsingExpression<R> expr;

	public NoneTerminal(int ruleIndex, Pattern<R> pattern) {
		this.ruleIndex = ruleIndex;
		this.pattern = pattern;
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
		return this.expr.isReturnable();
	}

	public void initExpr() {
		this.expr = this.pattern.invoke();
	}

	public int getRuleIndex() {
		return this.ruleIndex;
	}
}
