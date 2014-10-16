package aquarius.combinator.expression;

import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.ParserContext;
import aquarius.runtime.AquariusInputStream;
import aquarius.runtime.Result;
import static aquarius.runtime.Result.*;

/**
* try to match the expression. return matched result or null
* -> expr ?
* @author skgchxngsxyz-opensuse
 * @param <E>
*
*/
public class Optional<E> implements ParsingExpression<java.util.Optional<E>> {
	private final ParsingExpression<E> expr;

	public Optional(ParsingExpression<E> expr) {
		this.expr = expr;
	}

	public ParsingExpression<E> getExpr() {
		return this.expr;
	}

	@Override
	public String toString() {
		return this.expr.toString() + "?";
	}

	@Override
	public Result<java.util.Optional<E>> parse(ParserContext context) {
		AquariusInputStream input = context.getInput();
		int pos = input.getPosition();

		Result<E> result = this.expr.parse(context);
		if(result.isFailure()) {
			input.setPosition(pos);	// roll back position
			return of(java.util.Optional.empty());
		}
		return of(java.util.Optional.of(result.get()));
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitOptional(this);
	}
}