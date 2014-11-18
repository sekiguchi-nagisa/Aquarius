package aquarius.expression;

import aquarius.AquariusInputStream;
import aquarius.ExpressionVisitor;
import aquarius.ParserContext;
import aquarius.Token;

/**
* try to match sub expression sequence and return matched result as one string.
* -> < expr1 expr2 ... exprN >
* @author skgchxngsxyz-opensuse
*
*/
public class Capture extends ParsingExpression<Token> {	// extended expression type
	private final ParsingExpression<?>[] exprs;

	@SafeVarargs
	public Capture(ParsingExpression<?>... exprs) {
		this.exprs = exprs;
	}

	public ParsingExpression<?>[] getExprs() {
		return this.exprs;
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append('<');
		final int size = this.exprs.length;
		for(int i = 0; i < size; i++) {
			if(i > 0) {
				sBuilder.append(' ');
			}
			sBuilder.append(this.exprs[i]);
		}
		sBuilder.append('>');
		return sBuilder.toString();
	}

	@Override
	public boolean parse(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();
		for(ParsingExpression<?> e : this.exprs) {
			if(!e.parse(context)) {
				input.setPosition(pos);
				return false;
			}
		}
		context.pushValue(input.createToken(pos));
		return true;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitCapture(this);
	}

	@Override
	public boolean isReturnable() {
		return true;
	}
}