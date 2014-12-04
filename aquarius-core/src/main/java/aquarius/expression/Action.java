package aquarius.expression;

import aquarius.AquariusInputStream;
import aquarius.ExpressionVisitor;
import aquarius.ParserContext;
import aquarius.action.FailedActionException;
import aquarius.action.ParsingAction;
import aquarius.action.ParsingAction.*;
import aquarius.misc.Utils;

/**
* try to match the expression, if success execute action. preceding expression result
* is treated as the argument of action. return the result of action.
* -> expr { action }
* @author skgchxngsxyz-opensuse
 * @param <A>
 * @param <A>
*
*/
public class Action<R, A> implements ParsingExpression<R> {	// extended expression type
	private final ParsingExpression<A> expr;
	private final ParsingAction<R, A> action;
	private final boolean returnable;

	public Action(ParsingExpression<A> expr, ParsingAction<R, A> action) {
		this.expr = expr;
		this.action = action;
		this.returnable = action instanceof Mapper;
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
	public boolean parseImpl(ParserContext context) {
		// parser preceding expression
		if(!this.expr.parseImpl(context)) {
			return false;
		}

		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();

		// invoke action
		try {
			if(this.action instanceof Mapper) {
				context.pushValue(
					((Mapper<R, A>) this.action).invoke(context, (A) context.popValue()));
			} else {
				((Consumer<A>) this.action).invoke(context, (A) context.popValue());
			}
			return true;
		} catch(FailedActionException e) {
			context.pushFailure(pos, e);
			return false;
		} catch(Exception e) {
			return Utils.propagate(e);
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