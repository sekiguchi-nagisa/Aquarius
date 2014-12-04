package aquarius.expression;

import aquarius.ExpressionVisitor;
import aquarius.ParserContext;
import aquarius.misc.Tuple4;
import static aquarius.misc.Tuples.*;

public class Sequence4<A, B, C, D> implements ParsingExpression<Tuple4<A, B, C, D>> {
	private final Tuple4<ParsingExpression<A>, ParsingExpression<B>, 
			ParsingExpression<C>, ParsingExpression<D>> exprs;
	private final boolean returnable;

	public Sequence4(ParsingExpression<A> a, ParsingExpression<B> b, 
			ParsingExpression<C> c, ParsingExpression<D> d) {
		this.exprs = of(a, b, c, d);
		this.returnable = a.isReturnable() || b.isReturnable() 
				|| c.isReturnable() || d.isReturnable();
	}

	public Tuple4<ParsingExpression<A>, ParsingExpression<B>, 
			ParsingExpression<C>, ParsingExpression<D>> getExprs() {
		return this.exprs;
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(this.exprs.get0());
		sBuilder.append(' ');
		sBuilder.append(this.exprs.get1());
		sBuilder.append(' ');
		sBuilder.append(this.exprs.get2());
		sBuilder.append(' ');
		sBuilder.append(this.exprs.get3());
		return sBuilder.toString();
	}

	@Override
	public boolean parseImpl(ParserContext context) {
		if(this.exprs.get0().parseImpl(context)) {
			@SuppressWarnings("unchecked")
			A a = (A) context.popValue();

			if(this.exprs.get1().parseImpl(context)) {
				@SuppressWarnings("unchecked")
				B b = (B) context.popValue();

				if(this.exprs.get2().parseImpl(context)) {
					@SuppressWarnings("unchecked")
					C c = (C) context.popValue();

					if(this.exprs.get3().parseImpl(context)) {
						@SuppressWarnings("unchecked")
						D d = (D) context.popValue();

						if(this.returnable) {
							context.pushValue(of(a, b, c, d));
						}
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitSequence4(this);
	}

	@Override
	public boolean isReturnable() {
		return this.returnable;
	}
}