package aquarius.combinator.expression;

import static aquarius.runtime.Failure.inOneZore;
import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.ParserContext;
import aquarius.runtime.AquariusInputStream;
import aquarius.runtime.Failure;
import aquarius.runtime.ParsedResult;
import aquarius.runtime.ResultList;

/**
* match one or more repetition of the expression. return matched results as array
* -> expr +
* @author skgchxngsxyz-opensuse
*
*/
public class OneMore extends CompoundExpr {
	public OneMore(ParsingExpression expr) {
		super(expr);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitOneMore(this);
	}

	@Override
	public String toString() {
		return this.expr.toString() + "+";
	}

	@Override
	public ParsedResult parse(ParserContext context) {
		AquariusInputStream input = context.getInput();
		ResultList list = new ResultList();
		while(true) {
			int pos = input.getPosition();

			ParsedResult result = this.getExpr().parse(context);
			if(result instanceof Failure) {
				if(list.isEmpty()) {
					return inOneZore(input, this);
				}
				input.setPosition(pos);	// roll back position
				break;
			}
			list.add(result);
		}
		return list;
	}
}