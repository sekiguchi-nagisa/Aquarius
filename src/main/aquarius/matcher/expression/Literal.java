package aquarius.matcher.expression;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.ParserContext;
import aquarius.runtime.AquariusInputStream;
import aquarius.runtime.Result;
import static aquarius.runtime.Result.*;
import static aquarius.util.Utf8Util.*;
import aquarius.runtime.Token;

/**
* try to match string literal. return matched string
* -> 'literal'
* @author skgchxngsxyz-opensuse
*
*/
public class Literal implements ParsingExpression<Token> {
	private final int[] targetCodes;

	public Literal(String target) {
		this.targetCodes = toUtfCodes(target);
	}

	public int[] getTargetCodes() {
		return this.targetCodes;
	}

	public String getTarget() {
		return codesToString(this.targetCodes);
	}

	@Override
	public String toString() {
		return "'" + this.getTarget() + "'";
	}

	@Override
	public Result<Token> parse(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();

		if(pos == input.getInputSize()) {
			return inEOF(input, this);
		}

		for(int code : this.targetCodes) {
			if(code != input.fetch()) {
				try {
					return inLiteral(input, this, pos);
				} finally {
					input.setPosition(pos);
				}
			}
			input.consume();
		}
		return of(input.createToken(pos));
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitLiteral(this);
	}
}