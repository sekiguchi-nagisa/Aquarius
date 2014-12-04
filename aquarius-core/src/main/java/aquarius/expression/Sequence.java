package aquarius.expression;

import aquarius.ExpressionVisitor;
import aquarius.ParserContext;

/**
* try to match the sequence of expressions and return matched results as array.
* -> expr1 expr2 ... exprN
* @author skgchxngsxyz-opensuse
*
*/
public class Sequence implements ParsingExpression<Void> {
	private final ParsingExpression<?>[] exprs;

	@SafeVarargs
	public Sequence(ParsingExpression<?>... exprs) {
		this.exprs = exprs;
	}

	public ParsingExpression<?>[] getExprs() {
		return this.exprs;
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		final int size = this.exprs.length;
		for(int i = 0; i < size; i++) {
			if(i > 0) {
				sBuilder.append(' ');
			}
			sBuilder.append(this.exprs[i]);
		}
		return sBuilder.toString();
	}

	@Override
	public boolean parseImpl(ParserContext context) {
		for(ParsingExpression<?> e : this.exprs) {
			if(!e.parseImpl(context)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitSequence(this);
	}

	@Override
	public boolean isReturnable() {
		return false;
	}
}