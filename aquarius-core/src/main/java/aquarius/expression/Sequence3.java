package aquarius.expression;

import aquarius.AquariusInputStream;
import aquarius.ExpressionVisitor;
import aquarius.ParserContext;
import aquarius.misc.Tuple3;
import static aquarius.misc.Tuples.*;

public class Sequence3<A, B, C> implements ParsingExpression<Tuple3<A, B, C>> {
	private final Tuple3<ParsingExpression<A>, ParsingExpression<B>, ParsingExpression<C>> exprs;
	private final boolean returnable;

	public Sequence3(ParsingExpression<A> a, ParsingExpression<B> b, ParsingExpression<C> c) {
		this.exprs = of(a, b, c);
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

		if(this.exprs.get0().parse(context)) {
			@SuppressWarnings("unchecked")
			A a = (A) context.popValue();

			if(this.exprs.get1().parse(context)) {
				@SuppressWarnings("unchecked")
				B b = (B) context.popValue();

				if(this.exprs.get2().parse(context)) {
					@SuppressWarnings("unchecked")
					C c = (C) context.popValue();

					if(this.returnable) {
						context.pushValue(of(a, b, c));
					}
					return true;
				}
			}
		}
		input.setPosition(pos);
		return false;
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