package aquarius.runtime;

import aquarius.combinator.expression.AndPredict;
import aquarius.combinator.expression.Any;
import aquarius.combinator.expression.CharSet;
import aquarius.combinator.expression.NotPredict;
import aquarius.combinator.expression.OneMore;
import aquarius.combinator.expression.Literal;

public class Failure implements ParsedResult {
	private final String message;
	private final int currentPos;

	public Failure(int currentPos, String message) {
		this.currentPos = currentPos;
		this.message = message;
	}

	public Failure(Failure failure) {
		this.currentPos = failure.getFailurePos();
		this.message = "mismatch all of choice. longest matched: " + failure.getMessage();
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

	// failure creator api
	public final static Failure inEOF(AquariusInputStream input, Literal expr) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("require text: ");
		sBuilder.append(expr.getTarget());
		sBuilder.append(", but reach End of File");
		return new Failure(input.getPosition(), sBuilder.toString());
	}

	public final static Failure inEOF(AquariusInputStream input, Any expr) {
		return new Failure(input.getPosition(), "require any character, but reach End of File");
	}

	public final static Failure inEOF(AquariusInputStream input, CharSet expr) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("require chars: ");
		sBuilder.append(expr);
		sBuilder.append(", but reach End of File");
		return new Failure(input.getPosition(), sBuilder.toString());
	}

	public final static Failure inLiteral(AquariusInputStream input, Literal expr, int startPos) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("require text: ");
		sBuilder.append(expr.getTarget());
		sBuilder.append(", but is: ");
		sBuilder.append(input.createToken(startPos));
		return new Failure(input.getPosition(), sBuilder.toString());
	}

	public final static Failure inCharSet(AquariusInputStream input, CharSet expr, int ch) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("require chars: ");
		sBuilder.append(expr);
		sBuilder.append(", but is: ");
		sBuilder.append((char) ch);
		return new Failure(input.getPosition(), sBuilder.toString());
	}

	public final static Failure inOneZore(AquariusInputStream input, OneMore expr) {
		return new Failure(input.getPosition(), "require at least one pattern: " + expr);
	}

	public final static Failure inAnd(AquariusInputStream input, AndPredict expr) {
		return new Failure(input.getPosition(), "failed And predicate");
	}

	public final static Failure inNot(AquariusInputStream input, NotPredict expr) {
		return new Failure(input.getPosition(), "failed Not predicate");
	}
}
