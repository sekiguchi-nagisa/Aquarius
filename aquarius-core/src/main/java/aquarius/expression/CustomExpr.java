package aquarius.expression;

import aquarius.AquariusInputStream;
import aquarius.ExpressionVisitor;
import aquarius.ParserContext;
import aquarius.action.FailedActionException;
import aquarius.misc.Utils;

@FunctionalInterface
public interface CustomExpr<R> extends ParsingExpression<R> {
	public default <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitCustomExpr(this);
	}

	public default boolean parseImpl(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();

		try {
			context.pushValue(this.apply(context));
			return true;
		} catch(FailedActionException e) {
			context.pushFailure(pos, e);
			return false;
		} catch(Exception e) {
			return Utils.propagate(e);
		}
	}

	public default boolean isReturnable() {
		return true;
	}

	public R apply(ParserContext context) throws FailedActionException, Exception;
}
