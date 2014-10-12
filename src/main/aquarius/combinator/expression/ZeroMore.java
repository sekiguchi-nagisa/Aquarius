package aquarius.combinator.expression;

import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.ParserContext;
import aquarius.runtime.AquariusInputStream;
import aquarius.runtime.Failure;
import aquarius.runtime.ParsedResult;
import aquarius.runtime.ResultList;

/**
* match zero or more repetition of the expression. return matched results as array
* -> expr *
* @author skgchxngsxyz-opensuse
*
*/
public class ZeroMore extends CompoundExpr {
	public ZeroMore(ParsingExpression expr) {
		super(expr);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitZeroMore(this);
	}

	@Override
	public String toString() {
		return this.expr.toString() + "*";
	}

	@Override
	public ParsedResult parse(ParserContext context) {
		AquariusInputStream input = context.getInput();
		ResultList list = new ResultList();
		while(true) {
			int pos = input.getPosition();

			ParsedResult result = this.getExpr().parse(context);
			if(result instanceof Failure) {
				input.setPosition(pos);	// roll back position
				break;
			}
			list.add(result);
		}
		return list.isEmpty() ? ParsedResult.NULL_RESULT : list;
	}
}