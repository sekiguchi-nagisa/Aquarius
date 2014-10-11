package aquarius.combinator.expression;

import aquarius.combinator.ExpressionVisitor;

/**
* try to match any one character. return mached character
* -> .
* @author skgchxngsxyz-opensuse
*
*/
public class Any implements ParsingExpression {
	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitAny(this);
	}

	@Override
	public String toString() {
		return ".";
	}
}