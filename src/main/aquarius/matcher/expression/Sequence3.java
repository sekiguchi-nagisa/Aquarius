package aquarius.matcher.expression;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.ParserContext;
import aquarius.misc.Tuple3;
import aquarius.runtime.AquariusInputStream;

public class Sequence3<A, B, C> implements ParsingExpression<Tuple3<A, B, C>> {
	private final Tuple3<ParsingExpression<A>, ParsingExpression<B>, ParsingExpression<C>> exprs;
	private final boolean returnable;

	public Sequence3(ParsingExpression<A> a, ParsingExpression<B> b, ParsingExpression<C> c) {
		this.exprs = new Tuple3<>(a, b, c);
		this.returnable = a.isReturnable() || b.isReturnable() || c.isReturnable();
	}

	public Tuple3<ParsingExpression<A>, ParsingExpression<B>, ParsingExpression<C>> getExprs() {
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
		return sBuilder.toString();
	}

	@Override
	public boolean parse(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();

		// 1
		if(!this.exprs.get0().parse(context)) {
			input.setPosition(pos);
			return false;
		}
		@SuppressWarnings("unchecked")
		A a = (A) context.popValue();

		// 2
		if(!this.exprs.get1().parse(context)) {
			input.setPosition(pos);
			return false;
		}
		@SuppressWarnings("unchecked")
		B b = (B) context.popValue();

		// 3
		if(!this.exprs.get2().parse(context)) {
			input.setPosition(pos);
			return false;
		}
		@SuppressWarnings("unchecked")
		C c = (C) context.popValue();

		if(this.returnable) {
			context.pushValue(new Tuple3<A, B, C>(a, b, c));
		}
		return true;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitSequence3(this);
	}

	@Override
	public boolean isReturnable() {
		return this.returnable;
	}
}