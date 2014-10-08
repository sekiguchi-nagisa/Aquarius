package aquarius.runtime.expression;

import aquarius.runtime.ExpressionVisitor;

/**
* try to match sub expression. return matched result
* -> ( expression )
* @author skgchxngsxyz-opensuse
*
*/
public class SubExpr extends CompoundExpr {
	public SubExpr(ParsingExpression expr) {
		super(expr);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitSubExpr(this);
	}

	@Override
	public String toString() {
		return "(" + this.expr.toString() + ")";
	}
}