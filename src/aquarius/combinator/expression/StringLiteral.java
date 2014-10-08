package aquarius.combinator.expression;

import aquarius.combinator.ExpressionVisitor;

/**
* try to match string literal. return matched string
* -> 'literal'
* @author skgchxngsxyz-opensuse
*
*/
public class StringLiteral implements ParsingExpression {
	private final String target;

	public StringLiteral(String target) {
		this.target = target;
	}

	public String getTarget() {
		return this.target;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitString(this);
	}

	@Override
	public String toString() {
		return "'" + this.target + "'";
	}
}