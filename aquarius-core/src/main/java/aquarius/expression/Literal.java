package aquarius.expression;

import aquarius.AquariusInputStream;
import aquarius.ExpressionVisitor;
import aquarius.ParserContext;
import static aquarius.misc.Utf8Util.*;

/**
* try to match utf8 string literal.
* -> 'literal'
* @author skgchxngsxyz-opensuse
*
*/
public class Literal implements ParsingExpression<Void> {
	protected final int[] targetCodes;

	public static Literal newLiteral(String target) {
		int[] codes = toUtf8Codes(target);
		for(int code: codes) {
			if(!isAsciiCode(code)) {
				return new Literal(codes);
			}
		}
		return new AsciiLiteral(codes);
	}

	public Literal(String target) {
		this(toUtf8Codes(target));
	}

	protected Literal(int[] targetCodes) {
		this.targetCodes = targetCodes;
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
	public boolean parseImpl(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		for(int code : this.targetCodes) {
			if(code != input.fetch()) {
				context.pushFailure(input.getPosition(), this);
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