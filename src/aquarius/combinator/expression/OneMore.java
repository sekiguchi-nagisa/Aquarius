package aquarius.combinator.expression;

import aquarius.combinator.ExpressionVisitor;

/**
* match one or more repetition of the expression. return matched results as array
* -> expr +
* @author skgchxngsxyz-opensuse
*
*/
public class OneMore extends CompoundExpr {
	public OneMore(ParsingExpression expr) {
		super(expr);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitOneMore(this);
	}

	@Override
	public String toString() {
		return this.expr.toString() + "+";
	}
}