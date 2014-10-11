package aquarius.combinator.expression;

import aquarius.combinator.ExpressionVisitor;

/**
* try to match the expression. return matched result or null
* -> expr ?
* @author skgchxngsxyz-opensuse
*
*/
public class Optional extends CompoundExpr {
	public Optional(ParsingExpression expr) {
		super(expr);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitOptional(this);
	}

	@Override
	public String toString() {
		return this.expr.toString() + "?";
	}
}