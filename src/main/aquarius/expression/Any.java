package aquarius.expression;

import aquarius.AquariusInputStream;
import aquarius.ExpressionVisitor;
import aquarius.ParserContext;

/**
* try to match any one character. return mached character
* -> .
* @author skgchxngsxyz-opensuse
*
*/
public class Any extends ParsingExpression<Void> {
	@Override
	public String toString() {
		return ".";
	}

	@Override
	public boolean parse(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();

		if(pos == input.getInputSize()) {
			context.pushFailure(pos, this);
			return false;
		}
		input.consume();
		return true;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitAny(this);
	}

	@Override
	public boolean isReturnable() {
		return false;
	}
}