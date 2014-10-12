package aquarius.combinator.expression;

import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.ParserContext;
import aquarius.runtime.AquariusInputStream;
import aquarius.runtime.Failure;
import aquarius.runtime.ParsedResult;

/**
* try to match sub expression sequence and return matched result as one string.
* -> < expr1 expr2 ... exprN >
* @author skgchxngsxyz-opensuse
*
*/
public class Capture extends ListExpr {	// extended expression type
	public Capture(ParsingExpression... exprs) {
		super(exprs);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitCapture(this);
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append('<');
		final int size = this.exprList.size();
		for(int i = 0; i < size; i++) {
			if(i > 0) {
				sBuilder.append(' ');
			}
			sBuilder.append(this.exprList.get(i));
		}
		sBuilder.append('>');
		return sBuilder.toString();
	}

	@Override
	public ParsedResult parse(ParserContext context) {
		AquariusInputStream input = context.getInput();
		int pos = input.getPosition();
		for(ParsingExpression e : this.getExprList()) {
			ParsedResult result = e.parse(context);
			if(result instanceof Failure) {
				return result;
			}
		}
		return input.createToken(pos);
	}
}