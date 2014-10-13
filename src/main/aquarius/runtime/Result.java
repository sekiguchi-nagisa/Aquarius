package aquarius.runtime;

import java.util.List;
import java.util.NoSuchElementException;

import aquarius.combinator.expression.AndPredict;
import aquarius.combinator.expression.Any;
import aquarius.combinator.expression.CharSet;
import aquarius.combinator.expression.Literal;
import aquarius.combinator.expression.NotPredict;
import aquarius.combinator.expression.OneMore;

public interface Result<E> {
	public default boolean isFailure() {
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
		sBuilder.append(expr.getTarget());
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
		sBuilder.append("require text: ");
		sBuilder.append(expr.getTarget());
		sBuilder.append(", but is: ");
		sBuilder.append(input.createToken(startPos));
		return new Failure<Token>(input.getPosition(), sBuilder.toString());
	}

	public static Failure<Token> inCharSet(AquariusInputStream input, CharSet expr, int ch) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("require chars: ");
		sBuilder.append(expr);
		sBuilder.append(", but is: ");
		sBuilder.append((char) ch);
		return new Failure<Token>(input.getPosition(), sBuilder.toString());
	}

	public static <R> Failure<List<R>> inOneMore(AquariusInputStream input, OneMore<R> expr) {
		return new Failure<List<R>>(input.getPosition(), "require at least one pattern: " + expr);
	}

	public static Failure<Void> inAnd(AquariusInputStream input, AndPredict expr) {
		return new Failure<Void>(input.getPosition(), "failed And predicate");
	}

	public static Failure<Void> inNot(AquariusInputStream input, NotPredict expr) {
		return new Failure<Void>(input.getPosition(), "failed Not predicate");
	}
}