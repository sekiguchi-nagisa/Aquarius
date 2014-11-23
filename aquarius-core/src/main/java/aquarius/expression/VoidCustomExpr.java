package aquarius.expression;

import aquarius.AquariusInputStream;
import aquarius.ExpressionVisitor;
import aquarius.ParserContext;
import aquarius.action.FailedActionException;
import aquarius.misc.Utils;

@FunctionalInterface
public interface VoidCustomExpr extends ParsingExpression<Void> {
	public default <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitVoidCustomExpr(this);
	}

	public default boolean parse(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();

		try {
			this.apply(context);
			return true;
		} catch(FailedActionException e) {
			input.setPosition(pos);
			context.pushFailure(pos, e);
			return false;
		} catch(Exception e) {
			return Utils.propagate(e);
		}
	}

	public default boolean isReturnable() {
		return false;
	}

	public void apply(ParserContext context) throws FailedActionException, Exception;
}
