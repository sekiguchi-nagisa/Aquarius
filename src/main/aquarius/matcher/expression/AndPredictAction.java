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
public class AndPredictAction<A> implements ParsingExpression<Void> {	// extended expression type
	private final PredictiveAction<A> action;

	public AndPredictAction(PredictiveAction<A> action) {
		this.action = action;
	}

	public PredictiveAction<A> getAction() {
		return this.action;
	}

	@Override
	public String toString() {
		return "&{ action }";
	}

	@Override
	public Result<Void> parse(ParserContext context) {
		throw new RuntimeException("unsuppored: " + this.getClass());	//TODO:
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitAndPredictAction(this);
	}
}