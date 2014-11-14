package aquarius.matcher.expression;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.ParserContext;
import aquarius.matcher.ParsingAction.ParsingActionNoReturn;
import aquarius.matcher.ParsingAction.ParsingActionReturn;

public abstract class ParsingExpression<R> {
	public abstract <T> T accept(ExpressionVisitor<T> visitor);

	public abstract boolean parse(ParserContext context);

	public abstract boolean isReturnable();

	public  <E> Action<E, R> action(ParsingActionReturn<E, R> action) {
		return new Action<>(this, action);
	}

	public Action<Void, R> actionNoRet(ParsingActionNoReturn<R> action) {
		return new Action<>(this, action);
	}

	public final ZeroMore<R> zeroMore() {
		return new ZeroMore<>(this);
	}

	public final OneMore<R> oneMore() {
		return new OneMore<>(this);
	}

	public final Optional<R> opt() {
		return new Optional<>(this);
	}
}
