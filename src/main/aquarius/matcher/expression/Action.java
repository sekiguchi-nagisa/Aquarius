package aquarius.matcher.expression;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.FailedActionException;
import aquarius.matcher.ParserContext;
import aquarius.matcher.ParsingAction;
import aquarius.matcher.ParsingAction.*;
import aquarius.runtime.AquariusInputStream;

/**
* try to match the expression, if success execute action. preceding expression result
* is treated as the argument of action. return the result of action.
* -> expr { action }
* @author skgchxngsxyz-opensuse
 * @param <A>
 * @param <A>
*
*/
public class Action<R, A> extends ParsingExpression<R> {	// extended expression type
	private final ParsingExpression<A> expr;
	private final ParsingAction<R, A> action;
	private final boolean returnable;

	public Action(ParsingExpression<A> expr, ParsingAction<R, A> action) {
		this.expr = expr;
		this.action = action;
		this.returnable = action instanceof ParsingActionReturn;
	}

	public ParsingExpression<A> getExpr() {
		return this.expr;
	}

	public ParsingAction<R, A> getAction() {
		return this.action;
	}

	@Override
	public String toString() {
		return "{ action }";
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean parse(ParserContext context) {
		// parser preceding expression
		if(!this.expr.parse(context)) {
			return false;
		}

		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();

		// invoke action
		try {
			if(this.action instanceof ParsingActionReturn) {
				context.pushValue(
					((ParsingActionReturn<R, A>) this.action).invoke(context, (A) context.popValue()));
			} else {
				((ParsingActionNoReturn<A>) this.action).invoke(context, (A) context.popValue());
			}
			return true;
		} catch(FailedActionException e) {
			input.setPosition(pos);
			context.pushFailure(pos, e);
			return false;
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitAction(this);
	}

	@Override
	public boolean isReturnable() {
		return this.returnable;
	}
}