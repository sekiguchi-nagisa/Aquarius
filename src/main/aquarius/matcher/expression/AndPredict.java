package aquarius.matcher.expression;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.ParserContext;
import aquarius.runtime.AquariusInputStream;

/**
* try to match the expression. if success, not advance parsing position.
* not return value.
* otherwise, match failed
* -> & expr 
* @author skgchxngsxyz-opensuse
*
*/
public class AndPredict implements ParsingExpression<Void> {
	private final ParsingExpression<?> expr;

	public AndPredict(ParsingExpression<?> expr) {
		this.expr = expr;
	}

	public ParsingExpression<?> getExpr() {
		return this.expr;
	}

	@Override
	public String toString() {
		return "&" + this.expr.toString();
	}

	@Override
	public boolean parse(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();
		try {
			if(this.expr.parse(context)) {
				/**
				 * if prediction is success, return true 
				 */
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
		return visitor.visitAndPredict(this);
	}

	@Override
	public boolean isReturnable() {
		return false;
	}
}