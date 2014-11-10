package aquarius.matcher.expression;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.ParserContext;
import aquarius.misc.Tuple4;
import aquarius.runtime.AquariusInputStream;

public class Sequence4<A, B, C, D> implements ParsingExpression<Tuple4<A, B, C, D>> {
	private final Tuple4<ParsingExpression<A>, ParsingExpression<B>, 
			ParsingExpression<C>, ParsingExpression<D>> exprs;
	private final boolean returnable;

	public Sequence4(ParsingExpression<A> a, ParsingExpression<B> b, 
			ParsingExpression<C> c, ParsingExpression<D> d) {
		this.exprs = new Tuple4<>(a, b, c, d);
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

		if(this.returnable) {
			context.pushValue(new Tuple4<A, B, C, D>(a, b, c, d));
		}
		return true;
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