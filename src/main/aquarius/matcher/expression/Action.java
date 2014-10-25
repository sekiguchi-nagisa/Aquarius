package aquarius.matcher.expression;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.FailedActionException;
import aquarius.matcher.ParserContext;
import aquarius.matcher.ParsingAction;
import aquarius.runtime.AquariusInputStream;
import aquarius.runtime.Result;
import static aquarius.runtime.Result.*;

/**
* try to match the expression, if success execute action. preceding expression result
* is treated as the argument of action. return the result of action.
* -> expr { action }
* @author skgchxngsxyz-opensuse
 * @param <A>
*
*/
public class Action<R, A> implements ParsingExpression<R> {	// extended expression type
	private final ParsingExpression<A> expr;
	private final ParsingAction<A, R> action;

	public Action(ParsingExpression<A> expr, ParsingAction<A, R> action) {
		this.expr = expr;
		this.action = action;
	}

	public ParsingExpression<A> getExpr() {
		return this.expr;
	}

	public ParsingAction<A, R> getAction() {
		return this.action;
	}

	@Override
	public String toString() {
		return this.expr + "{ action }";
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result<R> parse(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();

		// evaluate preceding expression
		Result<A> result = this.getExpr().parse(context);
		if(result.isFailure()) {
			return (Result<R>) result;
		}
		int curPos = input.getPosition();

		// invoke action
		try {
			return this.getAction().invoke(context, result.get());
		} catch(FailedActionException e) {
			input.setPosition(pos);
			return inAction(curPos, this, e);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitAction(this);
	}
}