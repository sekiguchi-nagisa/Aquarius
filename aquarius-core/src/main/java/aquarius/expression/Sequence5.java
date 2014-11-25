package aquarius.expression;

import aquarius.AquariusInputStream;
import aquarius.ExpressionVisitor;
import aquarius.ParserContext;
import aquarius.misc.Tuple5;
import static aquarius.misc.Tuples.*;

public class Sequence5<A, B, C, D, E> implements ParsingExpression<Tuple5<A, B, C, D, E>> {
	private final Tuple5<ParsingExpression<A>, ParsingExpression<B>, 
			ParsingExpression<C>, ParsingExpression<D>, ParsingExpression<E>> exprs;
	private final boolean returnable;

	public Sequence5(ParsingExpression<A> a, ParsingExpression<B> b, 
			ParsingExpression<C> c, ParsingExpression<D> d, ParsingExpression<E> e) {
		this.exprs = of(a, b, c, d, e);
		this.returnable = a.isReturnable() || b.isReturnable() || 
				c.isReturnable() || d.isReturnable() || e.isReturnable();
	}

	public Tuple5<ParsingExpression<A>, ParsingExpression<B>, 
			ParsingExpression<C>, ParsingExpression<D>, ParsingExpression<E>> getExprs() {
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
		sBuilder.append(' ');
		sBuilder.append(this.exprs.get4());
		return sBuilder.toString();
	}

	@Override
	public boolean parse(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();

		if(this.exprs.get0().parse(context)) {
			@SuppressWarnings("unchecked")
			A a = (A) context.popValue();

			if(this.exprs.get1().parse(context)) {
				@SuppressWarnings("unchecked")
				B b = (B) context.popValue();

				if(this.exprs.get2().parse(context)) {
					@SuppressWarnings("unchecked")
					C c = (C) context.popValue();

					if(this.exprs.get3().parse(context)) {
						@SuppressWarnings("unchecked")
						D d = (D) context.popValue();

						if(this.exprs.get4().parse(context)) {
							@SuppressWarnings("unchecked")
							E e = (E) context.popValue();

							if(this.returnable) {
								context.pushValue(of(a, b, c, d, e));
							}
							return true;
						}
					}
				}
			}
		}
		input.setPosition(pos);
		return false;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitSequence5(this);
	}

	@Override
	public boolean isReturnable() {
		return this.returnable;
	}
}