package aquarius.combinator.expression;

import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.ParserContext;
import aquarius.combinator.ParsingAction;
import aquarius.runtime.Result;

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
		// evaluate preceding expression
		Result<A> result = this.getExpr().parse(context);
		if(result.isFailure()) {
			return (Result<R>) result;
		}

		// invoke action
		return this.getAction().invoke(result.get());	// may be Failure
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitAction(this);
	}
}