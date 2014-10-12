package aquarius.combinator.expression;

import static aquarius.runtime.Failure.inEOF;
import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.ParserContext;
import aquarius.runtime.AquariusInputStream;
import aquarius.runtime.ParsedResult;

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

	@Override
	public ParsedResult parse(ParserContext context) {
		AquariusInputStream input = context.getInput();
		int pos = input.getPosition();

		if(pos == input.getInputSize()) {
			return inEOF(input, this);
		}
		input.consume();
		return input.createToken(pos);
	}
}