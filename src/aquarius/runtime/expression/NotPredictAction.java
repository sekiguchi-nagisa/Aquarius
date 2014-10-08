package aquarius.runtime.expression;

import aquarius.runtime.ExpressionVisitor;
import aquarius.runtime.PredictiveAction;

/**
* execute semantic action. if return value is failed, not advance parsing position.
* otherwise, match failed. mpt return value
* -> ! { action }
* @author skgchxngsxyz-opensuse
*
*/
public class NotPredictAction implements ParsingExpression {	// extended expression type
	private final PredictiveAction action;

	public NotPredictAction(PredictiveAction action) {
		this.action = action;
	}

	public PredictiveAction getAction() {
		return this.action;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitNotPredictAction(this);
	}

	@Override
	public String toString() {
		return "!{ action }";
	}
}