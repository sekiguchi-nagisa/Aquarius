package aquarius.matcher;

import aquarius.matcher.expression.ParsingExpression;

public class NoneTerminal<R> extends ParsingExpression<R> {
	private final int ruleIndex;
	private final ParsingExpression<R> expr;

	public NoneTerminal(int ruleIndex, ParsingExpression<R> expr) {
		this.ruleIndex = ruleIndex;
		this.expr = expr;
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
		// TODO Auto-generated method stub
		return false;
	}

}
