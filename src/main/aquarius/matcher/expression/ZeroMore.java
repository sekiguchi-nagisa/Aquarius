package aquarius.matcher.expression;

import java.util.ArrayList;
import java.util.List;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.ParserContext;
import aquarius.runtime.AquariusInputStream;
import aquarius.runtime.Result;
import static aquarius.runtime.Result.*;

/**
* match zero or more repetition of the expression. return matched results as array
* -> expr *
* @author skgchxngsxyz-opensuse
 * @param <E>
*
*/
public class ZeroMore<E> implements ParsingExpression<List<E>> {
	private final ParsingExpression<E> expr;

	public ZeroMore(ParsingExpression<E> expr) {
		this.expr = expr;
	}

	public ParsingExpression<E> getExpr() {
		return this.expr;
	}

	@Override
	public String toString() {
		return this.expr.toString() + "*";
	}

	@Override
	public Result<List<E>> parse(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		List<E> list = new ArrayList<>();
		while(true) {
			int pos = input.getPosition();

			Result<E> result = this.expr.parse(context);
			if(result.isFailure()) {
				input.setPosition(pos);	// roll back position
				break;
			}
			list.add(result.get());
		}
		return of(list);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitZeroMore(this);
	}
}