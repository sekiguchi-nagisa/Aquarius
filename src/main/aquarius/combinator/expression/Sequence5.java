package aquarius.combinator.expression;

import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.ParserContext;
import aquarius.runtime.Result;
import aquarius.util.Tuple5;
import static aquarius.runtime.Result.*;

public class Sequence5<A, B, C, D, E> implements ParsingExpression<Tuple5<A, B, C, D, E>> {
	private final Tuple5<ParsingExpression<A>, ParsingExpression<B>, 
			ParsingExpression<C>, ParsingExpression<D>, ParsingExpression<E>> exprs;

	public Sequence5(ParsingExpression<A> a, ParsingExpression<B> b, 
			ParsingExpression<C> c, ParsingExpression<D> d, ParsingExpression<E> e) {
		this.exprs = new Tuple5<>(a, b, c, d, e);
	}

	public Tuple5<ParsingExpression<A>, ParsingExpression<B>, 
			ParsingExpression<C>, ParsingExpression<D>, ParsingExpression<E>> getExprs() {
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
		sBuilder.append(' ');
		sBuilder.append(this.exprs.get4());
		sBuilder.append(' ');
		sBuilder.append(this.exprs.get5());
		return sBuilder.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result<Tuple5<A, B, C, D, E>> parse(ParserContext context) {
		// 1
		Result<A> result1 = this.exprs.get1().parse(context);
		if(result1.isFailure()) {
			return (Result<Tuple5<A, B, C, D, E>>) result1;
		}

		// 2
		Result<B> result2 = this.exprs.get2().parse(context);
		if(result2.isFailure()) {
			return (Result<Tuple5<A, B, C, D, E>>) result2;
		}

		// 3
		Result<C> result3 = this.exprs.get3().parse(context);
		if(result3.isFailure()) {
			return (Result<Tuple5<A, B, C, D, E>>) result3;
		}

		// 4
		Result<D> result4 = this.exprs.get4().parse(context);
		if(result4.isFailure()) {
			return (Result<Tuple5<A, B, C, D, E>>) result4;
		}

		// 5
		Result<E> result5 = this.exprs.get5().parse(context);
		if(result5.isFailure()) {
			return (Result<Tuple5<A, B, C, D, E>>) result5;
		}
		return of(new Tuple5<>(result1.get(), result2.get(), 
				result3.get(), result4.get(), result5.get()));
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitSequence5(this);
	}
}