package aquarius.combinator.expression;

import aquarius.combinator.ExpressionVisitor;

/**
* try to match the expression. if not success, not advance parsing position.
* otherwise, match failed. not return value.
* -> ! expr
* @author skgchxngsxyz-opensuse
*
*/
public class NotPredict extends CompoundExpr {
	public NotPredict(ParsingExpression expr) {
		super(expr);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitNotPredict(this);
	}

	@Override
	public String toString() {
		return "!" + this.expr.toString();
	}
}