package aquarius.matcher.expression;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.ParserContext;
import aquarius.matcher.PredictiveAction;
import aquarius.runtime.Result;

/**
* execute semantic action. if return value is not failed, not advance parsing position.
* otherwise, match failed. not return value
* -> & { action }
* @author skgchxngsxyz-opensuse
 * @param <A>
*
*/
public class AndPredictAction<A> implements ParsingExpression<A> {	// extended expression type
	private final ParsingExpression<A> precedingExpr;
	private final PredictiveAction<A> action;

	public AndPredictAction(ParsingExpression<A> precedingExpr, PredictiveAction<A> action) {
		this.precedingExpr = precedingExpr;
		this.action = action;
	}

	public ParsingExpression<A> getPrecedingExpr() {
		return this.precedingExpr;
	}

	public PredictiveAction<A> getAction() {
		return this.action;
	}

	@Override
	public String toString() {
		return "&{ action }";
	}

	@Override
	public Result<A> parse(ParserContext context) {
		throw new RuntimeException("unsuppored: " + this.getClass());	//TODO:
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitAndPredictAction(this);
	}
}