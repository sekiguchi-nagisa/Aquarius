package aquarius.matcher;

import aquarius.matcher.expression.Action;
import aquarius.matcher.expression.AndPredict;
import aquarius.matcher.expression.AndPredictAction;
import aquarius.matcher.expression.Any;
import aquarius.matcher.expression.Capture;
import aquarius.matcher.expression.CharSet;
import aquarius.matcher.expression.Choice;
import aquarius.matcher.expression.Empty;
import aquarius.matcher.expression.Literal;
import aquarius.matcher.expression.NotPredict;
import aquarius.matcher.expression.NotPredictAction;
import aquarius.matcher.expression.OneMore;
import aquarius.matcher.expression.Optional;
import aquarius.matcher.expression.ParsingExpression;
import aquarius.matcher.expression.Sequence;
import aquarius.matcher.expression.Sequence2;
import aquarius.matcher.expression.Sequence3;
import aquarius.matcher.expression.Sequence4;
import aquarius.matcher.expression.Sequence5;
import aquarius.matcher.expression.ZeroMore;

public final class Expressions {
	private Expressions(){}

	public final static Literal str(String target) {
		return new Literal(target);
	}

	public final static Any any() {
		return new Any();
	}

	public final static CharSet ch(int ...chars) {
		return new CharSet(chars);
	}

	public final static <R> ZeroMore<R> zeroMore(ParsingExpression<R> expr) {
		return new ZeroMore<>(expr);
	}

	public final static <R> OneMore<R> oneMore(ParsingExpression<R> expr) {
		return new OneMore<>(expr);
	}

	public final static <R> Optional<R> opt(ParsingExpression<R> expr) {
		return new Optional<>(expr);
	}

	public final static AndPredict<Void> and(ParsingExpression<?> expr) {
		return new AndPredict<>(Empty.EMPTY, expr);
	}

	public final static NotPredict<Void> not(ParsingExpression<?> expr) {
		return new NotPredict<>(Empty.EMPTY, expr);
	}

	@SafeVarargs
	public final static <R> Sequence<R> seq(ParsingExpression<R>... exprs) {
		return new Sequence<>(exprs);
	}

	public final static <A, B> Sequence2<A, B> seq2(ParsingExpression<A> a, ParsingExpression<B> b) {
		return new Sequence2<>(a, b);
	}

	public final static <A, B, C> Sequence3<A, B, C> seq3(ParsingExpression<A> a, 
			ParsingExpression<B> b, ParsingExpression<C> c) {
		return new Sequence3<>(a, b, c);
	}

	public final static <A, B, C, D> Sequence4<A, B, C, D> seq4(ParsingExpression<A> a, 
			ParsingExpression<B> b, ParsingExpression<C> c, ParsingExpression<D> d) {
		return new Sequence4<>(a, b, c, d);
	}

	public final static <A, B, C, D, E> Sequence5<A, B, C, D, E> seq5(ParsingExpression<A> a, 
			ParsingExpression<B> b, ParsingExpression<C> c, 
			ParsingExpression<D> d, ParsingExpression<E> e) {
		return new Sequence5<>(a, b, c, d, e);
	}

	@SafeVarargs
	public final static <R> Choice<R> choice(ParsingExpression<R>... exprs) {
		return new Choice<>(exprs);
	}

	public final static AndPredictAction<Void> andAction(PredictiveAction<Void> action) {
		return new AndPredictAction<>(Empty.EMPTY, action);
	}

	public final static NotPredictAction<Void> notAction(PredictiveAction<Void> action) {
		return new NotPredictAction<>(Empty.EMPTY, action);
	}

	public final static Capture $(ParsingExpression<?>... exprs) {
		return new Capture(exprs);
	}

	public final static <R> Action<R, Void> action(ParsingAction<Void, R> action) {
		return new Action<>(Empty.EMPTY, action);
	}
}