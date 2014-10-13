package aquarius.combinator.expression;

import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.ParserContext;
import aquarius.runtime.AquariusInputStream;
import aquarius.runtime.Result;
import static aquarius.runtime.Result.*;
import aquarius.runtime.Token;

/**
* try to match sub expression sequence and return matched result as one string.
* -> < expr1 expr2 ... exprN >
* @author skgchxngsxyz-opensuse
*
*/
public class Capture implements ParsingExpression<Token> {	// extended expression type
	private final ParsingExpression<?>[] exprs;

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

	@SuppressWarnings("unchecked")
	@Override
	public Result<Token> parse(ParserContext context) {
		AquariusInputStream input = context.getInput();
		int pos = input.getPosition();
		for(ParsingExpression<?> e : this.exprs) {
			Result<?> result = e.parse(context);
			if(result.isFailure()) {
				return (Result<Token>) result;
			}
		}
		return of(input.createToken(pos));
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitCapture(this);
	}
}