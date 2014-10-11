package aquarius.combinator.expression;

import aquarius.combinator.ExpressionVisitor;

/**
* try to match string literal. return matched string
* -> 'literal'
* @author skgchxngsxyz-opensuse
*
*/
public class Literal implements ParsingExpression {
	private final String target;

	public Literal(String target) {
		this.target = target;
	}

	public String getTarget() {
		return this.target;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitLiteral(this);
	}

	@Override
	public String toString() {
		return "'" + this.target + "'";
	}
}