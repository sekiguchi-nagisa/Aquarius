package aquarius.expression;

import aquarius.AquariusInputStream;
import aquarius.ExpressionVisitor;
import aquarius.Failure;
import aquarius.ParserContext;

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
	private final boolean returnable;

	@SafeVarargs
	public Choice(ParsingExpression<R> ...exprs) {
		this.exprs = exprs;
		this.returnable = exprs[0].isReturnable();
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
	public boolean parse(ParserContext context) {
		AquariusInputStream input = context.getInputStream();

		Failure longestMatched = null;
		int pos = input.getPosition();
		for(ParsingExpression<R> e : this.exprs) {
			if(!e.parse(context)) {
				Failure failure = context.popFailure();
				if(longestMatched == null || longestMatched.getFailurePos() < failure.getFailurePos()) {
					longestMatched = failure;
				}
				input.setPosition(pos);	// roll back position
				continue;
			}
			return true;
		}
		context.pushFailure(longestMatched);
		return false;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitChoice(this);
	}

	@Override
	public boolean isReturnable() {
		return this.returnable;
	}
}