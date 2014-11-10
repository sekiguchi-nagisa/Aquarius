package aquarius.matcher.expression;

import java.util.ArrayList;
import java.util.List;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.ParserContext;
import aquarius.runtime.AquariusInputStream;

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
		AquariusInputStream input = context.getInputStream();
		int count = 0;
		List<R> result = this.returnable ? new ArrayList<>() : null;
		while(true) {
			int pos = input.getPosition();
			count++;
			if(!this.expr.parse(context)) {
				if(count == 1) {
					return false;
				}
				input.setPosition(pos);	// roll back position
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
		return visitor.visitOneMore(this);
	}

	@Override
	public boolean isReturnable() {
		return this.returnable;
	}
}