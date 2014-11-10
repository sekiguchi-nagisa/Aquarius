package aquarius.matcher.expression;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.ParserContext;
import aquarius.matcher.ParsingAction;

public interface ParsingExpression<R> {
	public <T> T accept(ExpressionVisitor<T> visitor);

	public boolean parse(ParserContext context);

	public boolean isReturnable();

	public default <E> Action<E, R> action(ParsingAction<E, R> action) {
		return new Action<E, R>(this, action);
	}
}
