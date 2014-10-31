package aquarius.matcher.expression;

import static aquarius.runtime.Result.*;
import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.ParserContext;
import aquarius.matcher.PredictiveAction;
import aquarius.runtime.AquariusInputStream;
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
	private final ParsingExpression<A> precedingExpr;
	private final PredictiveAction<A> action;

	public NotPredictAction(ParsingExpression<A> precedingExpr, PredictiveAction<A> action) {
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
		return "!{ action }";
	}

	@Override
	public Result<A> parse(ParserContext context) {
		// parse preceding expression
		int precedingPos = context.getInputStream().getPosition();
		Result<A> precedingResult = this.precedingExpr.parse(context);
		if(precedingResult.isFailure()) {
			return precedingResult;
		}

		// invoke action
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();

		try {
			if(!this.action.invoke(context, precedingResult.get())) {
				/**
				 * if prediction is failed, return preceding result 
				 */
				input.setPosition(pos);
				return precedingResult;
			}
			try {
				return inNot(pos, this);
			} finally {
				input.setPosition(precedingPos);
			}
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitNotPredictAction(this);
	}
}