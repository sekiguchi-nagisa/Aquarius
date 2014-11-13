package aquarius.matcher.expression;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.ParserContext;
import aquarius.matcher.PredictiveAction;
import aquarius.runtime.AquariusInputStream;

/**
* execute semantic action. if return value is not failed, not advance parsing position.
* otherwise, match failed. not return value
* -> & { action }
* @author skgchxngsxyz-opensuse
*
*/
public class PredictAction extends ParsingExpression<Void> {	// extended expression type
	private final PredictiveAction action;

	public PredictAction(PredictiveAction action) {
		this.action = action;
	}

	public PredictiveAction getAction() {
		return this.action;
	}

	@Override
	public String toString() {
		return "&{ action }";
	}

	@Override
	public boolean parse(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();

		try {
			if(this.action.invoke(context)) {
				/**
				 * if prediction is success, return true
				 */
				input.setPosition(pos);
				return true;
			}
			try {
				context.pushFailure(pos, this);
				return false;
			} finally {
				input.setPosition(pos);
			}
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitPredictAction(this);
	}

	@Override
	public boolean isReturnable() {
		return false;
	}
}