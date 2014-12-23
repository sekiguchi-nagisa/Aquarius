package aquarius.expression;

import aquarius.AquariusInputStream;
import aquarius.ExpressionVisitor;
import aquarius.ParserContext;

/**
* try to match any one utf8 character.
* -> .
* @author skgchxngsxyz-opensuse
*
*/
public class Any implements ParsingExpression<Void> {
	@Override
	public String toString() {
		return ".";
	}

	@Override
	public boolean parseImpl(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		if(input.fetch() == AquariusInputStream.EOF) {
			context.pushFailure(input.getPosition(), this);
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