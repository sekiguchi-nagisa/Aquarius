package aquarius.expression;

import aquarius.ExpressionVisitor;
import aquarius.ParserContext;

/**
* try to match the expression. return matched result as java.util.Optional
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
	public boolean parseImpl(ParserContext context) {
		context.setFailureCreation(false);
		this.expr.parse(context);
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