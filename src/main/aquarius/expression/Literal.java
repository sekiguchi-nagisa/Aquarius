package aquarius.expression;

import aquarius.AquariusInputStream;
import aquarius.ExpressionVisitor;
import aquarius.ParserContext;
import static aquarius.misc.Utf8Util.*;

/**
* try to match string literal. return matched string
* -> 'literal'
* @author skgchxngsxyz-opensuse
*
*/
public class Literal extends ParsingExpression<Void> {
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
	public boolean parse(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();

		if(pos == input.getInputSize()) {
			context.pushFailure(pos, this);
			return false;
		}

		for(int code : this.targetCodes) {
			if(code != input.fetch()) {
				context.pushFailure(pos, this);
				input.setPosition(pos);
				return false;
			}
			input.consume();
		}
		return true;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitLiteral(this);
	}

	@Override
	public boolean isReturnable() {
		return false;
	}
}