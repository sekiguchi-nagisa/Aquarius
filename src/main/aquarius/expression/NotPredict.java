package aquarius.expression;

import aquarius.AquariusInputStream;
import aquarius.ExpressionVisitor;
import aquarius.ParserContext;

/**
* try to match the expression. if not success, not advance parsing position.
* otherwise, match failed. not return value.
* -> ! expr
* @author skgchxngsxyz-opensuse
*
*/
public class NotPredict extends ParsingExpression<Void> {
	private final ParsingExpression<?> expr;

	public NotPredict(ParsingExpression<?> expr) {
		this.expr = expr;
	}

	public ParsingExpression<?> getExpr() {
		return this.expr;
	}

	@Override
	public String toString() {
		return "!" + this.expr.toString();
	}

	@Override
	public boolean parse(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();

		try {
			if(!this.expr.parse(context)) {
				/**
				 * if prediction is failed, return true
				 */
				context.popFailure();
				return true;
			}
			context.pushFailure(pos, this);
			return false;
		} finally {
			input.setPosition(pos);
		}
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitNotPredict(this);
	}

	@Override
	public boolean isReturnable() {
		return false;
	}
}