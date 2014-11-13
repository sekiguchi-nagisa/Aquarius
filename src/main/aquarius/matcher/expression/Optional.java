package aquarius.matcher.expression;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.ParserContext;
import aquarius.runtime.AquariusInputStream;

/**
* try to match the expression. return matched result or null
* -> expr ?
* @author skgchxngsxyz-opensuse
 * @param <R>
*
*/
public class Optional<R> extends ParsingExpression<java.util.Optional<R>> {
	private final ParsingExpression<R> expr;
	private final boolean returnable;

	public Optional(ParsingExpression<R> expr) {
		this.expr = expr;
		this.returnable = expr.isReturnable();
	}

	public ParsingExpression<R> getExpr() {
		return this.expr;
	}

	@Override
	public String toString() {
		return this.expr.toString() + "?";
	}

	@Override
	public boolean parse(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();
		if(!this.expr.parse(context)) {
			input.setPosition(pos);	// roll back position
			context.popFailure();
		}
		if(this.returnable) {
			context.pushValue(java.util.Optional.ofNullable(context.popValue()));
		}
		return true;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitOptional(this);
	}

	@Override
	public boolean isReturnable() {
		return this.returnable;
	}
}