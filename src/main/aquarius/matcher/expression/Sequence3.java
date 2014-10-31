package aquarius.matcher.expression;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.ParserContext;
import aquarius.misc.Tuple3;
import aquarius.runtime.AquariusInputStream;
import aquarius.runtime.Result;
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
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();

		// 1
		Result<A> result1 = this.exprs.get1().parse(context);
		if(result1.isFailure()) {
			try {
				return (Result<Tuple3<A, B, C>>) result1;
			} finally {
				input.setPosition(pos);
			}
		}

		// 2
		Result<B> result2 = this.exprs.get2().parse(context);
		if(result2.isFailure()) {
			try {
				return (Result<Tuple3<A, B, C>>) result2;
			} finally {
				input.setPosition(pos);
			}
		}

		// 3
		Result<C> result3 = this.exprs.get3().parse(context);
		if(result3.isFailure()) {
			try {
				return (Result<Tuple3<A, B, C>>) result3;
			} finally {
				input.setPosition(pos);
			}
		}
		return of(new Tuple3<>(result1.get(), result2.get(), result3.get()));
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitSequence3(this);
	}
}