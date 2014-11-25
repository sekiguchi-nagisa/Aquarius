package aquarius.expression;

import java.util.LinkedList;
import java.util.List;

import aquarius.ExpressionVisitor;
import aquarius.ParserContext;

/**
* match one or more repetition of the expression. return matched results as array
* -> expr +
* @author skgchxngsxyz-opensuse
 * @param <R>
*
*/
public class OneMore<R> implements ParsingExpression<List<R>> {
	private final ParsingExpression<R> expr;
	private final boolean returnable;

	public OneMore(ParsingExpression<R> expr) {
		this.expr = expr;
		this.returnable = expr.isReturnable();
	}

	public ParsingExpression<R> getExpr() {
		return this.expr;
	}

	@Override
	public String toString() {
		return this.expr.toString() + "+";
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean parse(ParserContext context) {
		// match at least once
		if(!this.expr.parse(context)) {
			return false;
		}
		List<R> result = null;
		if(this.returnable) {
			result = new LinkedList<>();
			result.add((R) context.popValue());
		}

		context.setFailureCreation(false);
		while(this.expr.parse(context)) {
			if(this.returnable) {
				result.add((R) context.popValue());
			}
		}
		context.pushValue(result);
		context.setFailureCreation(true);
		return true;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitOneMore(this);
	}

	@Override
	public boolean isReturnable() {
		return this.returnable;
	}
}