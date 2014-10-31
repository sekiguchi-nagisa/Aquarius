package aquarius.matcher.expression;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.FailedActionException;
import aquarius.matcher.ParserContext;
import aquarius.matcher.ParsingActionWithoutArg;
import aquarius.runtime.AquariusInputStream;
import aquarius.runtime.Result;
import static aquarius.runtime.Result.*;

/**
* try to match the expression, if success execute action. preceding expression result
* is treated as the argument of action. return the result of action.
* -> expr { action }
* @author skgchxngsxyz-opensuse
 * @param <A>
*
*/
public class NoneArgAction<R> implements ParsingExpression<R> {	// extended expression type
	private final ParsingActionWithoutArg<R> action;

	public NoneArgAction(ParsingActionWithoutArg<R> action) {
		this.action = action;
	}

	public ParsingActionWithoutArg<R> getAction() {
		return this.action;
	}

	@Override
	public String toString() {
		return "{ action }";
	}

	@Override
	public Result<R> parse(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();

		// invoke action
		try {
			return of(this.getAction().invoke(context));
		} catch(FailedActionException e) {
			input.setPosition(pos);
			return inAction(pos, this, e);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitNoneArgAction(this);
	}
}