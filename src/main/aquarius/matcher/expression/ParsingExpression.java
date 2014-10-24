package aquarius.matcher.expression;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.ParserContext;
import aquarius.matcher.ParsingAction;
import aquarius.matcher.PredictiveAction;
import aquarius.runtime.Result;

public interface ParsingExpression<R> {
	public <T> T accept(ExpressionVisitor<T> visitor);

	public Result<R> parse(ParserContext context);

	// creator api
	public static Literal str(String target) {
		return new Literal(target);
	}

	public static Any any() {
		return new Any();
	}

	public static CharSet ch(int ...chars) {
		return new CharSet(chars);
	}

	public static <R> ZeroMore<R> zeroMore(ParsingExpression<R> expr) {
		return new ZeroMore<R>(expr);
	}

	public static <R> OneMore<R> oneMore(ParsingExpression<R> expr) {
		return new OneMore<R>(expr);
	}

	public static <R> Optional<R> opt(ParsingExpression<R> expr) {
		return new Optional<R>(expr);
	}

	public static AndPredict and(ParsingExpression<?> expr) {
		return new AndPredict(expr);
	}

	public static NotPredict not(ParsingExpression<?> expr) {
		return new NotPredict(expr);
	}

	@SafeVarargs
	public static <R> Sequence<R> seq(ParsingExpression<R>... exprs) {
		return new Sequence<R>(exprs);
	}

	public static <A, B> Sequence2<A, B> seq2(ParsingExpression<A> a, ParsingExpression<B> b) {
		return new Sequence2<A, B>(a, b);
	}

	public static <A, B, C> Sequence3<A, B, C> seq3(ParsingExpression<A> a, 
			ParsingExpression<B> b, ParsingExpression<C> c) {
		return new Sequence3<A, B, C>(a, b, c);
	}

	public static <A, B, C, D> Sequence4<A, B, C, D> seq4(ParsingExpression<A> a, 
			ParsingExpression<B> b, ParsingExpression<C> c, ParsingExpression<D> d) {
		return new Sequence4<A, B, C, D>(a, b, c, d);
	}

	public static <A, B, C, D, E> Sequence5<A, B, C, D, E> seq5(ParsingExpression<A> a, 
			ParsingExpression<B> b, ParsingExpression<C> c, 
			ParsingExpression<D> d, ParsingExpression<E> e) {
		return new Sequence5<A, B, C, D, E>(a, b, c, d, e);
	}

	@SafeVarargs
	public static <R> Choice<R> choice(ParsingExpression<R>... exprs) {
		return new Choice<R>(exprs);
	}

	public static <R, A> Action<R, A> action(ParsingExpression<A> expr, ParsingAction<A, R> action) {
		return new Action<R, A>(expr, action);
	}

	public static <A> AndPredictAction<A> andAction(PredictiveAction<A> action) {
		return new AndPredictAction<A>(action);
	}

	public static <A> NotPredictAction<A> notAction(PredictiveAction<A> action) {
		return new NotPredictAction<A>(action);
	}

	public static Capture $(ParsingExpression<?>... exprs) {
		return new Capture(exprs);
	}
}


