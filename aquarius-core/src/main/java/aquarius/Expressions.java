package aquarius;

import aquarius.action.PredictiveAction;
import aquarius.expression.AndPredict;
import aquarius.expression.Any;
import aquarius.expression.Capture;
import aquarius.expression.CharSet;
import aquarius.expression.Choice;
import aquarius.expression.Literal;
import aquarius.expression.NotPredict;
import aquarius.expression.OneMore;
import aquarius.expression.Optional;
import aquarius.expression.ParsingExpression;
import aquarius.expression.PredictAction;
import aquarius.expression.Sequence;
import aquarius.expression.Sequence2;
import aquarius.expression.Sequence3;
import aquarius.expression.Sequence4;
import aquarius.expression.Sequence5;
import aquarius.expression.ZeroMore;
import aquarius.misc.Tuple2;
import aquarius.misc.Tuple3;
import aquarius.misc.Tuple4;
import aquarius.misc.Tuple5;

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

	public final static CharSet ch(char... chars) {
		return new CharSet(chars);
	}

	public final static CharSet ch(int ...chars) {
		return new CharSet(chars);
	}

	public final static CharSet r(char start, char stop) {
		return new CharSet(new int[]{}).r(start, stop);
	}

	public final static CharSet r(int start, int stop) {
		return new CharSet(new int[]{}).r(start, stop);
	}

	// ZeroMore
	public final static <A, B> ZeroMore<Tuple2<A, B>> zeroMore(ParsingExpression<A> a, ParsingExpression<B> b) {
		return new ZeroMore<>(new Sequence2<>(a, b));
	}

	public final static <A, B, C> ZeroMore<Tuple3<A, B, C>> zeroMore(ParsingExpression<A> a, 
			ParsingExpression<B> b, ParsingExpression<C> c) {
		return new ZeroMore<>(new Sequence3<>(a, b, c));
	}

	public final static <A, B, C, D> ZeroMore<Tuple4<A, B, C, D>> zeroMore(ParsingExpression<A> a, 
			ParsingExpression<B> b, ParsingExpression<C> c, ParsingExpression<D> d) {
		return new ZeroMore<>(new Sequence4<>(a, b, c, d));
	}

	public final static <A, B, C, D, E> ZeroMore<Tuple5<A, B, C, D, E>> zeroMore(ParsingExpression<A> a, 
			ParsingExpression<B> b, ParsingExpression<C> c, ParsingExpression<D> d, ParsingExpression<E> e) {
		return new ZeroMore<>(new Sequence5<>(a, b, c, d, e));
	}

	// OneMore
	public final static <A, B> OneMore<Tuple2<A, B>> oneMore(ParsingExpression<A> a, ParsingExpression<B> b) {
		return new OneMore<>(new Sequence2<>(a, b));
	}

	public final static <A, B, C> OneMore<Tuple3<A, B, C>> oneMore(ParsingExpression<A> a, 
			ParsingExpression<B> b, ParsingExpression<C> c) {
		return new OneMore<>(new Sequence3<>(a, b, c));
	}

	public final static <A, B, C, D> OneMore<Tuple4<A, B, C, D>> oneMore(ParsingExpression<A> a, 
			ParsingExpression<B> b, ParsingExpression<C> c, ParsingExpression<D> d) {
		return new OneMore<>(new Sequence4<>(a, b, c, d));
	}

	public final static <A, B, C, D, E> OneMore<Tuple5<A, B, C, D, E>> oneMore(ParsingExpression<A> a, 
			ParsingExpression<B> b, ParsingExpression<C> c, ParsingExpression<D> d, ParsingExpression<E> e) {
		return new OneMore<>(new Sequence5<>(a, b, c, d, e));
	}

	// Optional
	public final static <A, B> Optional<Tuple2<A, B>> opt(ParsingExpression<A> a, ParsingExpression<B> b) {
		return new Optional<>(new Sequence2<>(a, b));
	}

	public final static <A, B, C> Optional<Tuple3<A, B, C>> opt(ParsingExpression<A> a, 
			ParsingExpression<B> b, ParsingExpression<C> c) {
		return new Optional<>(new Sequence3<>(a, b, c));
	}

	public final static <A, B, C, D> Optional<Tuple4<A, B, C, D>> opt(ParsingExpression<A> a, 
			ParsingExpression<B> b, ParsingExpression<C> c, ParsingExpression<D> d) {
		return new Optional<>(new Sequence4<>(a, b, c, d));
	}

	public final static <A, B, C, D, E> Optional<Tuple5<A, B, C, D, E>> opt(ParsingExpression<A> a, 
			ParsingExpression<B> b, ParsingExpression<C> c, ParsingExpression<D> d, ParsingExpression<E> e) {
		return new Optional<>(new Sequence5<>(a, b, c, d, e));
	}

	// And Predict
	public final static AndPredict and(ParsingExpression<?> expr) {
		return new AndPredict(expr);
	}

	public final static AndPredict and(ParsingExpression<?>... exprs) {
		return new AndPredict(new Sequence(exprs));
	}

	// Not Predict
	public final static NotPredict not(ParsingExpression<?> expr) {
		return new NotPredict(expr);
	}

	public final static NotPredict not(ParsingExpression<?>... exprs) {
		return new NotPredict(new Sequence(exprs));
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
	public final static <R> Choice<R> or(ParsingExpression<? extends R>... exprs) {
		return new Choice<>((ParsingExpression<R>[]) exprs);
	}

	public final static PredictAction predict(PredictiveAction action) {
		return new PredictAction(action);
	}

	@SafeVarargs
	public final static Capture $(ParsingExpression<?>... exprs) {
		return new Capture(exprs);
	}
}
