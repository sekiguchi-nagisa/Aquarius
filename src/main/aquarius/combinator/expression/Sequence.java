package aquarius.combinator.expression;

import java.util.ArrayList;
import java.util.List;

import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.ParserContext;
import aquarius.runtime.Result;
import static aquarius.runtime.Result.*;

/**
* try to match the sequence of expressions and return matched results as array.
* -> expr1 expr2 ... exprN
* @author skgchxngsxyz-opensuse
 * @param <R>
*
*/
public class Sequence<R> implements ParsingExpression<List<R>> {
	private final ParsingExpression<R>[] exprs;

	@SafeVarargs
	public Sequence(ParsingExpression<R>... exprs) {
		this.exprs = exprs;
	}

	public ParsingExpression<R>[] getExprs() {
		return this.exprs;
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		final int size = this.exprs.length;
		for(int i = 0; i < size; i++) {
			if(i > 0) {
				sBuilder.append(' ');
			}
			sBuilder.append(this.exprs[i]);
		}
		return sBuilder.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result<List<R>> parse(ParserContext context) {
		List<R> list = new ArrayList<>(this.exprs.length);
		for(ParsingExpression<R> e : this.exprs) {
			Result<R> result = e.parse(context);
			if(result.isFailure()) {
				return (Result<List<R>>) result;
			}
			list.add(result.get());
		}
		return of(list);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitSequence(this);
	}
}