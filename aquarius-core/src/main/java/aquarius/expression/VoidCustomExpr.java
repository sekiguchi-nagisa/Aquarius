package aquarius.expression;

import aquarius.AquariusInputStream;
import aquarius.ExpressionVisitor;
import aquarius.ParserContext;
import aquarius.action.FailedActionException;
import aquarius.misc.Utils;

/**
 * for user defined operator.
 * @author skgchxngsxyz-opensuse
 *
 */
@FunctionalInterface
public interface VoidCustomExpr extends ParsingExpression<Void> {
	public default <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitVoidCustomExpr(this);
	}

	public default boolean parseImpl(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();

		try {
			this.apply(context);
			return true;
		} catch(FailedActionException e) {
			context.pushFailure(pos, e);
			return false;
		} catch(Exception e) {
			return Utils.propagate(e);
		}
	}

	public default boolean isReturnable() {
		return false;
	}

	/**
	 * user definition operator implementation
	 * @param context
	 * @throws FailedActionException
	 * @throws Exception
	 */
	public void apply(ParserContext context) throws FailedActionException, Exception;
}
