package aquarius.matcher.expression;

import java.util.ArrayList;
import java.util.List;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.ParserContext;
import aquarius.runtime.AquariusInputStream;
import aquarius.runtime.Result;
import static aquarius.runtime.Result.*;

/**
* match one or more repetition of the expression. return matched results as array
* -> expr +
* @author skgchxngsxyz-opensuse
 * @param <R>
*
*/
public class OneMore<R> implements ParsingExpression<List<R>> {
	private final ParsingExpression<R> expr;

	public OneMore(ParsingExpression<R> expr) {
		this.expr = expr;
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
	public Result<List<R>> parse(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		List<R> list = new ArrayList<>();
		while(true) {
			int pos = input.getPosition();

			Result<R> result = this.expr.parse(context);
			if(result.isFailure()) {
				if(list.isEmpty()) {
					return (Result<List<R>>) result;
				}
				input.setPosition(pos);	// roll back position
				break;
			}
			list.add(result.get());
		}
		return of(list);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitOneMore(this);
	}
}