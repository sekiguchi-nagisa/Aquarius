package aquarius.combinator.expression;

import aquarius.combinator.ExpressionVisitor;

/**
* try to match the sequence of expressions and return matched results as array.
* -> expr1 expr2 ... exprN
* @author skgchxngsxyz-opensuse
*
*/
public class Sequence extends ListExpr {
	public Sequence(ParsingExpression... exprs) {
		super(exprs);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitSeq(this);
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		final int size = this.exprList.size();
		for(int i = 0; i < size; i++) {
			if(i > 0) {
				sBuilder.append(' ');
			}
			sBuilder.append(this.exprList.get(i));
		}
		return sBuilder.toString();
	}
}