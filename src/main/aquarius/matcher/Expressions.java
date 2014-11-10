package aquarius.matcher;

import aquarius.matcher.expression.AndPredict;
import aquarius.matcher.expression.NoArgAction;
import aquarius.matcher.expression.PredictAction;
import aquarius.matcher.expression.Any;
import aquarius.matcher.expression.Capture;
import aquarius.matcher.expression.CharSet;
import aquarius.matcher.expression.Choice;
import aquarius.matcher.expression.Literal;
import aquarius.matcher.expression.NotPredict;
import aquarius.matcher.expression.OneMore;
import aquarius.matcher.expression.Optional;
import aquarius.matcher.expression.ParsingExpression;
import aquarius.matcher.expression.Sequence;
import aquarius.matcher.expression.Sequence2;
import aquarius.matcher.expression.Sequence3;
import aquarius.matcher.expression.Sequence4;
import aquarius.matcher.expression.Sequence5;
import aquarius.matcher.expression.ZeroMore;

/**
 * helper methods for parsing expression construction
 * @author skgchxngsxyz-opensuse
 *
 */
public final class Expressions {
	private Expressions(){}

	public final static Any ANY = new Any();

	public final static Literal str(String target) {
		return new Literal(target);
	}

	public final static CharSet ch(int ...chars) {
		return new CharSet(chars);
	}

	public final static CharSet r(int start, int stop) {
		return new CharSet().r(start, stop);
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

	public final static AndPredict and(ParsingExpression<?> expr) {
		return new AndPredict(expr);
	}

	public final static NotPredict not(ParsingExpression<?> expr) {
		return new NotPredict(expr);
	}

	@SafeVarargs
	public final static Sequence seqN(ParsingExpression<?>... exprs) {
		return new Sequence(exprs);
	}

	public final static <A, B> Sequence2<A, B> seq(ParsingExpression<A> a, ParsingExpression<B> b) {
		return new Sequence2<>(a, b);
	}

	public final static <A, B, C> Sequence3<A, B, C> seq(ParsingExpression<A> a, ParsingExpression<B> b, 
			ParsingExpression<C> c) {
		return new Sequence3<>(a, b, c);
	}

	public final static <A, B, C, D> Sequence4<A, B, C, D> seq(ParsingExpression<A> a, ParsingExpression<B> b, 
			ParsingExpression<C> c, ParsingExpression<D> d) {
		return new Sequence4<>(a, b, c, d);
	}

	public final static <A, B, C, D, E> Sequence5<A, B, C, D, E> seq(ParsingExpression<A> a, ParsingExpression<B> b, 
			ParsingExpression<C> c, ParsingExpression<D> d, ParsingExpression<E> e) {
		return new Sequence5<>(a, b, c, d, e);
	}

	@SuppressWarnings("unchecked")
	@SafeVarargs
	public final static <R> Choice<R> choice(ParsingExpression<? extends R>... exprs) {
		return new Choice<R>((ParsingExpression<R>[]) exprs);
	}

	public final static PredictAction predict(PredictiveAction action) {
		return new PredictAction(action);
	}

	@SafeVarargs
	public final static Capture $(ParsingExpression<?>... exprs) {
		return new Capture(exprs);
	}

	public final static <R> NoArgAction<R> action(ParsingActionNoArg<R> action) {
		return new NoArgAction<R>(action);
	}
}
