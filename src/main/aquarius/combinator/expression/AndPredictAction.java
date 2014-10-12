package aquarius.combinator.expression;

import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.ParserContext;
import aquarius.combinator.PredictiveAction;
import aquarius.runtime.ParsedResult;

/**
* execute semantic action. if return value is not failed, not advance parsing position.
* otherwise, match failed. not return value
* -> & { action }
* @author skgchxngsxyz-opensuse
*
*/
public class AndPredictAction implements ParsingExpression {	// extended expression type
	private final PredictiveAction action;

	public AndPredictAction(PredictiveAction action) {
		this.action = action;
	}

	public PredictiveAction getAction() {
		return this.action;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitAndPredictAction(this);
	}

	@Override
	public String toString() {
		return "&{ action }";
	}

	@Override
	public ParsedResult parse(ParserContext context) {
		throw new RuntimeException("unsuppored: " + this.getClass());	//TODO:
	}
}