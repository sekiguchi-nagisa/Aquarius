package aquarius.combinator.expression;

import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.ParserContext;
import aquarius.runtime.Result;
import aquarius.util.Tuple3;
import static aquarius.runtime.Result.*;

public class Sequence3<A, B, C> implements ParsingExpression<Tuple3<A, B, C>> {
	private final Tuple3<ParsingExpression<A>, ParsingExpression<B>, ParsingExpression<C>> exprs;

	public Sequence3(ParsingExpression<A> a, ParsingExpression<B> b, ParsingExpression<C> c) {
		this.exprs = new Tuple3<>(a, b, c);
	}

	public Tuple3<ParsingExpression<A>, ParsingExpression<B>, ParsingExpression<C>> getExprs() {
		return this.exprs;
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(this.exprs.get1());
		sBuilder.append(' ');
		sBuilder.append(this.exprs.get2());
		sBuilder.append(' ');
		sBuilder.append(this.exprs.get3());
		return sBuilder.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result<Tuple3<A, B, C>> parse(ParserContext context) {
		// 1
		Result<A> result1 = this.exprs.get1().parse(context);
		if(result1.isFailure()) {
			return (Result<Tuple3<A, B, C>>) result1;
		}

		// 2
		Result<B> result2 = this.exprs.get2().parse(context);
		if(result2.isFailure()) {
			return (Result<Tuple3<A, B, C>>) result2;
		}

		// 3
		Result<C> result3 = this.exprs.get3().parse(context);
		if(result3.isFailure()) {
			return (Result<Tuple3<A, B, C>>) result3;
		}
		return of(new Tuple3<>(result1.get(), result2.get(), result3.get()));
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitSequence3(this);
	}
}