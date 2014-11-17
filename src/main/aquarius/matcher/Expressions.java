package aquarius.matcher;

import aquarius.matcher.ParsingActionNoArg.ParsingActionNoArgNoReturn;
import aquarius.matcher.ParsingActionNoArg.ParsingActionNoArgReturn;
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
	public final static <R> Choice<R> choice(ParsingExpression<? extends R>... exprs) {
		return new Choice<>((ParsingExpression<R>[]) exprs);
	}

	public final static PredictAction predict(PredictiveAction action) {
		return new PredictAction(action);
	}

	@SafeVarargs
	public final static Capture $(ParsingExpression<?>... exprs) {
		return new Capture(exprs);
	}

	public final static <R> NoArgAction<R> action(ParsingActionNoArgReturn<R> action) {
		return new NoArgAction<>(action);
	}

	public final static NoArgAction<Void> actionNoRet(ParsingActionNoArgNoReturn action) {
		return new NoArgAction<>(action);
	}
}
