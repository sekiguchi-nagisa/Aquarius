package aquarius.combinator.expression;

import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.ParsingAction;

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
}