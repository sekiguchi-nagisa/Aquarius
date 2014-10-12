package aquarius.combinator.expression;

import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.ParserContext;
import aquarius.combinator.PredictiveAction;
import aquarius.runtime.ParsedResult;

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

	@Override
	public ParsedResult parse(ParserContext context) {
		throw new RuntimeException("unsuppored: " + this.getClass());	//TODO:
	}
}