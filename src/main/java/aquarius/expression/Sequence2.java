package aquarius.expression;

import aquarius.AquariusInputStream;
import aquarius.ExpressionVisitor;
import aquarius.ParserContext;
import aquarius.misc.Tuple2;

public class Sequence2<A, B> extends ParsingExpression<Tuple2<A, B>> {
	private final Tuple2<ParsingExpression<A>, ParsingExpression<B>> exprs;
	private final boolean returnable;

	public Sequence2(ParsingExpression<A> a, ParsingExpression<B> b) {
		this.exprs = new Tuple2<>(a, b);
		this.returnable = a.isReturnable() || b.isReturnable();
	}

	public Tuple2<ParsingExpression<A>, ParsingExpression<B>> getExprs() {
		return this.exprs;
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(this.exprs.get0());
		sBuilder.append(' ');
		sBuilder.append(this.exprs.get1());
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

		if(this.returnable) {
			context.pushValue(new Tuple2<A, B>(a, b));
		}
		return true;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitSequence2(this);
	}

	@Override
	public boolean isReturnable() {
		return this.returnable;
	}
}