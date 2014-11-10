package aquarius.matcher.expression;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.ParserContext;
import aquarius.misc.Tuple5;
import aquarius.runtime.AquariusInputStream;

public class Sequence5<A, B, C, D, E> implements ParsingExpression<Tuple5<A, B, C, D, E>> {
	private final Tuple5<ParsingExpression<A>, ParsingExpression<B>, 
			ParsingExpression<C>, ParsingExpression<D>, ParsingExpression<E>> exprs;
	private final boolean returnable;

	public Sequence5(ParsingExpression<A> a, ParsingExpression<B> b, 
			ParsingExpression<C> c, ParsingExpression<D> d, ParsingExpression<E> e) {
		this.exprs = new Tuple5<>(a, b, c, d, e);
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

	@Override
	public boolean parse(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();

		// 1
		if(!this.exprs.get1().parse(context)) {
			input.setPosition(pos);
			return false;
		}
		@SuppressWarnings("unchecked")
		A a = (A) context.popValue();

		// 2
		if(!this.exprs.get2().parse(context)) {
			input.setPosition(pos);
			return false;
		}
		@SuppressWarnings("unchecked")
		B b = (B) context.popValue();

		// 3
		if(!this.exprs.get3().parse(context)) {
			input.setPosition(pos);
			return false;
		}
		@SuppressWarnings("unchecked")
		C c = (C) context.popValue();

		// 4
		if(!this.exprs.get4().parse(context)) {
			input.setPosition(pos);
			return false;
		}
		@SuppressWarnings("unchecked")
		D d = (D) context.popValue();
		

		// 5
		if(!this.exprs.get5().parse(context)) {
			input.setPosition(pos);
			return false;
		}
		@SuppressWarnings("unchecked")
		E e = (E) context.popValue();

		if(this.returnable) {
			context.pushValue(new Tuple5<A, B, C, D, E>(a, b, c, d, e));
		}
		return true;
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