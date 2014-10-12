package aquarius.combinator.expression;

import static aquarius.runtime.Failure.inEOF;
import static aquarius.runtime.Failure.inLiteral;
import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.ParserContext;
import aquarius.runtime.AquariusInputStream;
import aquarius.runtime.ParsedResult;

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

	@Override
	public ParsedResult parse(ParserContext context) {
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
		return input.createToken(pos);
	}
}