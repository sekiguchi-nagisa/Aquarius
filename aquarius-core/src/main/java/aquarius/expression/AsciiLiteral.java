package aquarius.expression;

import aquarius.AquariusInputStream;
import aquarius.ParserContext;

/**
 * try to match ascii string literal.
 * @author skgchxngsxyz-opensuse
 *
 */
public class AsciiLiteral extends Literal {
	public AsciiLiteral(int[] targetCodes) {
		super(targetCodes);
	}

	@Override
	public boolean parseImpl(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		for(int code : this.targetCodes) {
			if(code != input.fetchByte()) {
				context.pushFailure(input.getPosition(), this);
				return false;
			}
			input.consumeByte();
		}
		return true;
	}
}
