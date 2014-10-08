package aquarius.runtime.expression;

import aquarius.runtime.ExpressionVisitor;

/**
* try to match the expression. if success, not advance parsing position.
* not return value.
* otherwise, match failed
* -> & expr 
* @author skgchxngsxyz-opensuse
*
*/
public class AndPredict extends CompoundExpr {
	public AndPredict(ParsingExpression expr) {
		super(expr);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitAndPredict(this);
	}

	@Override
	public String toString() {
		return "&" + this.expr.toString();
	}
}