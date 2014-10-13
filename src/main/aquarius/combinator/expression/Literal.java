package aquarius.combinator.expression;

import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.ParserContext;
import aquarius.runtime.AquariusInputStream;
import aquarius.runtime.Result;
import static aquarius.runtime.Result.*;
import aquarius.runtime.Token;

/**
* try to match string literal. return matched string
* -> 'literal'
* @author skgchxngsxyz-opensuse
*
*/
public class Literal implements ParsingExpression<Token> {
	private final String target;

	public Literal(String target) {
		this.target = target;
	}

	public String getTarget() {
		return this.target;
	}

	@Override
	public String toString() {
		return "'" + this.target + "'";
	}

	@Override
	public Result<Token> parse(ParserContext context) {
		AquariusInputStream input = context.getInput();
		int pos = input.getPosition();

		if(pos == input.getInputSize()) {
			return inEOF(input, this);
		}

		String text = this.getTarget();
		final int size = text.length();
		for(int i = 0; i < size; i++) {
			if(text.charAt(i) != input.consume()) {
				return inLiteral(input, this, pos);
			}
		}
		return of(input.createToken(pos));
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitLiteral(this);
	}
}