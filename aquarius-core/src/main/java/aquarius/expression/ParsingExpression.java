package aquarius.expression;

import aquarius.ExpressionVisitor;
import aquarius.ParserContext;
import aquarius.action.ParsingAction.Consumer;
import aquarius.action.ParsingAction.Mapper;

public interface ParsingExpression<R> {
	public <T> T accept(ExpressionVisitor<T> visitor);

	public boolean parse(ParserContext context);

	public boolean isReturnable();

	public default <E> Action<E, R> map(Mapper<E, R> action) {
		return new Action<>(this, action);
	}

	public default Action<Void, R> consume(Consumer<R> action) {
		return new Action<>(this, action);
	}

	public default ZeroMore<R> zeroMore() {
		return new ZeroMore<>(this);
	}

	public default OneMore<R> oneMore() {
		return new OneMore<>(this);
	}

	public default Optional<R> opt() {
		return new Optional<>(this);
	}

	@SuppressWarnings("unchecked")
	public default Choice<R> or(ParsingExpression<? extends R> expr) {
		return new Choice<>(this, (ParsingExpression<R>) expr);
	}
}
