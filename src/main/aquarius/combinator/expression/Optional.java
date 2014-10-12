package aquarius.combinator.expression;

import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.ParserContext;
import aquarius.runtime.AquariusInputStream;
import aquarius.runtime.Failure;
import aquarius.runtime.ParsedResult;

/**
* try to match the expression. return matched result or null
* -> expr ?
* @author skgchxngsxyz-opensuse
*
*/
public class Optional extends CompoundExpr {
	public Optional(ParsingExpression expr) {
		super(expr);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitOptional(this);
	}

	@Override
	public String toString() {
		return this.expr.toString() + "?";
	}

	@Override
	public ParsedResult parse(ParserContext context) {
		AquariusInputStream input = context.getInput();
		int pos = input.getPosition();

		ParsedResult result = this.getExpr().parse(context);
		if(result instanceof Failure) {
			input.setPosition(pos);	// roll back position
			return ParsedResult.NULL_RESULT;
		}
		return result;
	}
}