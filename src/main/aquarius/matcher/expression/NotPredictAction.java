package aquarius.matcher.expression;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.ParserContext;
import aquarius.matcher.PredictiveAction;
import aquarius.runtime.Result;

/**
* execute semantic action. if return value is failed, not advance parsing position.
* otherwise, match failed. mpt return value
* -> ! { action }
* @author skgchxngsxyz-opensuse
 * @param <A>
*
*/
public class NotPredictAction<A> implements ParsingExpression<A> {	// extended expression type
	private final ParsingExpression<A> expr;
	private final PredictiveAction<A> action;

	public NotPredictAction(ParsingExpression<A> expr, PredictiveAction<A> action) {
		this.expr = expr;
		this.action = action;
	}

	public ParsingExpression<A> getExpr() {
		return this.expr;
	}

	public PredictiveAction<A> getAction() {
		return this.action;
	}

	@Override
	public String toString() {
		return "!{ action }";
	}

	@Override
	public Result<A> parse(ParserContext context) {
		throw new RuntimeException("unsuppored: " + this.getClass());	//TODO:
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitNotPredictAction(this);
	}
}