package aquarius.expression;

import java.util.LinkedList;
import java.util.List;

import aquarius.AquariusInputStream;
import aquarius.ExpressionVisitor;
import aquarius.ParserContext;

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
	public boolean parseImpl(ParserContext context) {
		List<R> result = this.returnable ? new LinkedList<R>() : null;

		context.setFailureCreation(false);
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();
		while(this.expr.parse(context)) {
			if(this.returnable) {
				result.add((R) context.popValue());
			}
			pos = input.getPosition();
		}
		input.setPosition(pos);
		context.pushValue(result);
		context.setFailureCreation(true);
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