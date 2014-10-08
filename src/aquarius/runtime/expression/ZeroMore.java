package aquarius.runtime.expression;

import aquarius.runtime.ExpressionVisitor;

/**
* match zero or more repetition of the expression. return matched results as array
* -> expr *
* @author skgchxngsxyz-opensuse
*
*/
public class ZeroMore extends CompoundExpr {
	public ZeroMore(ParsingExpression expr) {
		super(expr);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitZeroMore(this);
	}

	@Override
	public String toString() {
		return this.expr.toString() + "*";
	}
}