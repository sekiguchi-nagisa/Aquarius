package aquarius.combinator.expression;

import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.ParserContext;
import aquarius.combinator.ParsingAction;
import aquarius.runtime.Failure;
import aquarius.runtime.ParsedResult;

/**
* try to match the expression, if success execute action. preceding expression result
* is treated as the argument of action. return the result of action.
* -> expr { action }
* @author skgchxngsxyz-opensuse
*
*/
public class Action implements ParsingExpression {	// extended expression type
	private final ParsingExpression expr;
	private final ParsingAction action;

	public Action(ParsingExpression expr, ParsingAction action) {
		this.expr = expr;
		this.action = action;
	}

	public ParsingExpression getExpr() {
		return this.expr;
	}

	public ParsingAction getAction() {
		return this.action;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitAction(this);
	}

	@Override
	public String toString() {
		return this.expr + "{ action }";
	}

	@Override
	public ParsedResult parse(ParserContext context) {
		// evaluate preceding expression
		ParsedResult result = this.getExpr().parse(context);
		if(result instanceof Failure) {
			return result;
		}

		// invoke action
		return this.getAction().invoke(result);	// may be Failure
	}
}