package aquarius.combinator.expression;

import java.util.ArrayList;
import java.util.List;

import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.ParserContext;
import aquarius.runtime.AquariusInputStream;
import aquarius.runtime.Result;
import aquarius.runtime.Result.Failure;

/**
* try to match first expression and if failed try the second one ...
* return matched result.
* -> expr1 / expr2 / ... / exprN
* @author skgchxngsxyz-opensuse
 * @param <R>
*
*/
public class Choice<R> implements ParsingExpression<R> {
	private final ParsingExpression<R>[] exprs;

	@SafeVarargs
	public Choice(ParsingExpression<R> ...exprs) {
		this.exprs = exprs;
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		final int size = this.exprs.length;
		for(int i = 0; i < size; i++) {
			if(i > 0) {
				sBuilder.append(" / ");
			}
			sBuilder.append(this.exprs[i]);
		}
		return sBuilder.toString();
	}

	@Override
	public Result<R> parse(ParserContext context) {
		AquariusInputStream input = context.getInput();
		// for error report
		List<Failure<R>> failures = new ArrayList<>(this.exprs.length);

		int pos = input.getPosition();
		for(ParsingExpression<R> e : this.exprs) {
			Result<R> result = e.parse(context);
			if(result.isFailure()) {
				failures.add((Failure<R>) result);
				input.setPosition(pos);	// roll back position
				continue;
			}
			return result;
		}
		Failure<R> longestMatched = failures.get(0);
		for(Failure<R> failure : failures) {
			if(failure.getFailurePos() > longestMatched.getFailurePos()) {
				longestMatched = failure;
			}
		}
		return longestMatched;	// longe	//FIXME:
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitChoice(this);
	}
}