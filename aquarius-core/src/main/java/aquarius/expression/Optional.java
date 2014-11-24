package aquarius.expression;

import aquarius.AquariusInputStream;
import aquarius.ExpressionVisitor;
import aquarius.ParserContext;

/**
* try to match the expression. return matched result or null
* -> expr ?
* @author skgchxngsxyz-opensuse
 * @param <R>
*
*/
public class Optional<R> implements ParsingExpression<java.util.Optional<R>> {
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
		context.setFailureCreation(false);
		if(!this.expr.parse(context)) {
			input.setPosition(pos);	// roll back position
		}
		if(this.returnable) {
			context.pushValue(java.util.Optional.ofNullable(context.popValue()));
		}
		context.setFailureCreation(true);
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