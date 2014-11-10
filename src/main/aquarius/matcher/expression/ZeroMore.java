package aquarius.matcher.expression;

import java.util.ArrayList;
import java.util.List;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.ParserContext;
import aquarius.runtime.AquariusInputStream;

/**
* match zero or more repetition of the expression. return matched results as array
* -> expr *
* @author skgchxngsxyz-opensuse
 * @param <R>
*
*/
public class ZeroMore<R> implements ParsingExpression<List<R>> {
	private final ParsingExpression<R> expr;
	private final boolean returnable;

	public ZeroMore(ParsingExpression<R> expr) {
		this.expr = expr;
		this.returnable = expr.isReturnable();
	}

	public ParsingExpression<R> getExpr() {
		return this.expr;
	}

	@Override
	public String toString() {
		return this.expr.toString() + "*";
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean parse(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		List<R> result = this.returnable ? new ArrayList<>() : null;
		while(true) {
			int pos = input.getPosition();
			if(!this.expr.parse(context)) {
				input.setPosition(pos);	// roll back position
				context.popFailure();
				break;
			}
			if(this.returnable) {
				result.add((R) context.popValue());
			}
		}
		context.pushValue(result);
		return true;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitZeroMore(this);
	}

	@Override
	public boolean isReturnable() {
		return this.returnable;
	}
}