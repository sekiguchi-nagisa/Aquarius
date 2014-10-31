package aquarius.runtime;

import java.util.NoSuchElementException;

import aquarius.matcher.FailedActionException;
import aquarius.matcher.expression.Action;
import aquarius.matcher.expression.AndPredict;
import aquarius.matcher.expression.AndPredictAction;
import aquarius.matcher.expression.Any;
import aquarius.matcher.expression.CharSet;
import aquarius.matcher.expression.Literal;
import aquarius.matcher.expression.NoneArgAction;
import aquarius.matcher.expression.NotPredict;
import aquarius.matcher.expression.NotPredictAction;
import aquarius.misc.Utf8Util;

public interface Result<E> {
	public default boolean isFailure() {
		return false;
	}

	public default boolean isEmpty() {
		return false;
	}
	/**
	 * 
	 * @return
	 * may be null
	 */
	public default E get() {
		throw new NoSuchElementException();
	}

	public static <E> Result<E> of(E value) {
		return new ResultImpl<>(value);
	}

	public static <E> Result<E> empty() {
		return new ResultImpl<E>(null);
	}

	static class ResultImpl<E> implements Result<E> {
		private final E value;

		/**
		 * 
		 * @param value
		 * may be null
		 */
		private ResultImpl(E value) {
			this.value = value;
		}

		@Override
		public boolean isEmpty() {
			return this.value == null;
		}

		@Override
		public E get() {
			return this.value;
		}
	}

	public static class Failure<E> implements Result<E> {
		private final String message;
		private final int currentPos;

		private Failure(int currentPos, String message) {
			this.currentPos = currentPos;
			this.message = message;
		}

		@Override
		public boolean isFailure() {
			return true;
		}

		public String getMessage() {
			return this.message;
		}

		public int getFailurePos() {
			return this.currentPos;
		}

		@Override
		public String toString() {
			return "failure at " + this.currentPos + ": " + this.message;
		}
	}

	// failure creator api
	public static Failure<Token> inEOF(AquariusInputStream input, Literal expr) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("require text: ");
		sBuilder.append(expr);
		sBuilder.append(", but reach End of File");
		return new Failure<Token>(input.getPosition(), sBuilder.toString());
	}

	public static Failure<Token> inEOF(AquariusInputStream input, Any expr) {
		return new Failure<Token>(input.getPosition(), "require any character, but reach End of File");
	}

	public static Failure<Token> inEOF(AquariusInputStream input, CharSet expr) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("require chars: ");
		sBuilder.append(expr);
		sBuilder.append(", but reach End of File");
		return new Failure<Token>(input.getPosition(), sBuilder.toString());
	}

	public static Failure<Token> inLiteral(AquariusInputStream input, Literal expr, int startPos) {
		StringBuilder sBuilder = new StringBuilder();
		int pos = input.getPosition();
		sBuilder.append("require text: ");
		sBuilder.append(expr);
		sBuilder.append(", but is: ");
		sBuilder.append(input.createToken(startPos, pos == startPos ? startPos + 1 : pos).getText(input));
		return new Failure<Token>(pos, sBuilder.toString());
	}

	public static Failure<Token> inCharSet(AquariusInputStream input, CharSet expr, int ch) {
		StringBuilder sBuilder = new StringBuilder();
		int pos = input.getPosition();
		sBuilder.append("require chars: ");
		sBuilder.append(expr);
		sBuilder.append(", but is: ");
		sBuilder.append(Utf8Util.codeToString(ch));
		return new Failure<Token>(pos, sBuilder.toString());
	}

	public static <R> Failure<R> inAnd(int pos, AndPredict<R> expr) {
		return new Failure<R>(pos, "failed And predicate");
	}

	public static <R> Failure<R> inAnd(int pos, AndPredictAction<R> expr) {
		return new Failure<R>(pos, "failed And predicate");
	}

	public static <R> Failure<R> inNot(int pos, NotPredict<R> expr) {
		return new Failure<R>(pos, "failed Not predicate");
	}

	public static <R> Failure<R> inNot(int pos, NotPredictAction<R> expr) {
		return new Failure<R>(pos, "failed Not predicate");
	}

	public static <A, R> Failure<R> inAction(int pos, Action<R, A> expr, FailedActionException e) {
		return new Failure<>(pos, e.getMessage());
	}

	public static <R> Failure<R> inAction(int pos, NoneArgAction<R> expr, FailedActionException e) {
		return new Failure<>(pos, e.getMessage());
	}
}
