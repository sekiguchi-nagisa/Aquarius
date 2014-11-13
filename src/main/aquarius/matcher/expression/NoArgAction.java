package aquarius.matcher.expression;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.FailedActionException;
import aquarius.matcher.ParserContext;
import aquarius.matcher.ParsingActionNoArg;
import aquarius.matcher.ParsingActionNoArg.*;
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
public class NoArgAction<R> extends ParsingExpression<R> {	// extended expression type
	private final ParsingActionNoArg<R> action;
	private final boolean returnable;

	public NoArgAction(ParsingActionNoArg<R> action) {
		this.action = action;
		this.returnable = action instanceof ParsingActionNoArgReturn;
	}

	public ParsingActionNoArg<R> getAction() {
		return this.action;
	}

	@Override
	public String toString() {
		return "{ action }";
	}

	@Override
	public boolean parse(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();

		// invoke action
		try {
			if(this.action instanceof ParsingActionNoArgReturn) {
				context.pushValue(
					((ParsingActionNoArgReturn<R>) this.action).invoke(context));
			} else {
				((ParsingActionNoArgNoReturn) this.action).invoke(context);
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
		return visitor.visitNoArgAction(this);
	}

	@Override
	public boolean isReturnable() {
		return this.returnable;
	}
}