package aquarius.matcher.expression;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.ParserContext;
import aquarius.matcher.ParsingAction.ParsingActionNoReturn;
import aquarius.matcher.ParsingAction.ParsingActionReturn;

public interface ParsingExpression<R> {
	public <T> T accept(ExpressionVisitor<T> visitor);

	public boolean parse(ParserContext context);

	public boolean isReturnable();

	public default <E> Action<E, R> action(ParsingActionReturn<E, R> action) {
		return new Action<>(this, action);
	}

	public default Action<Void, R> actionNoRet(ParsingActionNoReturn<R> action) {
		return new Action<>(this, action);
	}
}
