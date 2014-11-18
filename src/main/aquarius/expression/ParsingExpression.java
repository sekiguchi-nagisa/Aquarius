package aquarius.expression;

import aquarius.ExpressionVisitor;
import aquarius.ParserContext;
import aquarius.action.ParsingAction.ParsingActionNoReturn;
import aquarius.action.ParsingAction.ParsingActionReturn;

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
