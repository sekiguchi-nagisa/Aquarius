package aquarius.combinator.expression;

import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.ParserContext;
import aquarius.runtime.Result;
import aquarius.util.Tuple2;
import static aquarius.runtime.Result.*;

public class Sequence2<A, B> implements ParsingExpression<Tuple2<A, B>> {
	private final Tuple2<ParsingExpression<A>, ParsingExpression<B>> exprs;

	public Sequence2(ParsingExpression<A> a, ParsingExpression<B> b) {
		this.exprs = new Tuple2<>(a, b);
	}

	public Tuple2<ParsingExpression<A>, ParsingExpression<B>> getExprs() {
		return this.exprs;
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(this.exprs.get1());
		sBuilder.append(' ');
		sBuilder.append(this.exprs.get2());
		return sBuilder.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result<Tuple2<A, B>> parse(ParserContext context) {
		// 1
		Result<A> result1 = this.exprs.get1().parse(context);
		if(result1.isFailure()) {
			return (Result<Tuple2<A, B>>) result1;
		}

		// 2
		Result<B> result2 = this.exprs.get2().parse(context);
		if(result2.isFailure()) {
			return (Result<Tuple2<A, B>>) result2;
		}
		return of(new Tuple2<>(result1.get(), result2.get()));
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitSequence2(this);
	}
}