package aquarius.matcher.expression;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.ParserContext;
import aquarius.runtime.AquariusInputStream;
import aquarius.runtime.Result;
import aquarius.util.Tuple4;
import static aquarius.runtime.Result.*;

public class Sequence4<A, B, C, D> implements ParsingExpression<Tuple4<A, B, C, D>> {
	private final Tuple4<ParsingExpression<A>, ParsingExpression<B>, 
			ParsingExpression<C>, ParsingExpression<D>> exprs;

	public Sequence4(ParsingExpression<A> a, ParsingExpression<B> b, 
			ParsingExpression<C> c, ParsingExpression<D> d) {
		this.exprs = new Tuple4<>(a, b, c, d);
	}

	public Tuple4<ParsingExpression<A>, ParsingExpression<B>, 
			ParsingExpression<C>, ParsingExpression<D>> getExprs() {
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
		return sBuilder.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result<Tuple4<A, B, C, D>> parse(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();

		// 1
		Result<A> result1 = this.exprs.get1().parse(context);
		if(result1.isFailure()) {
			try {
				return (Result<Tuple4<A, B, C, D>>) result1;
			} finally {
				input.setPosition(pos);
			}
		}

		// 2
		Result<B> result2 = this.exprs.get2().parse(context);
		if(result2.isFailure()) {
			try {
				return (Result<Tuple4<A, B, C, D>>) result2;
			} finally {
				input.setPosition(pos);
			}
		}

		// 3
		Result<C> result3 = this.exprs.get3().parse(context);
		if(result3.isFailure()) {
			try {
				return (Result<Tuple4<A, B, C, D>>) result3;
			} finally {
				input.setPosition(pos);
			}
		}

		// 4
		Result<D> result4 = this.exprs.get4().parse(context);
		if(result4.isFailure()) {
			try {
				return (Result<Tuple4<A, B, C, D>>) result4;
			} finally {
				input.setPosition(pos);
			}
		}
		return of(new Tuple4<>(result1.get(), result2.get(), 
				result3.get(), result4.get()));
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitSequence4(this);
	}
}