package aquarius.combinator.expression;

import static aquarius.runtime.Failure.inAnd;
import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.ParserContext;
import aquarius.runtime.AquariusInputStream;
import aquarius.runtime.Failure;
import aquarius.runtime.ParsedResult;

/**
* try to match the expression. if success, not advance parsing position.
* not return value.
* otherwise, match failed
* -> & expr 
* @author skgchxngsxyz-opensuse
*
*/
public class AndPredict extends CompoundExpr {
	public AndPredict(ParsingExpression expr) {
		super(expr);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitAndPredict(this);
	}

	@Override
	public String toString() {
		return "&" + this.expr.toString();
	}

	@Override
	public ParsedResult parse(ParserContext context) {
		AquariusInputStream input = context.getInput();
		int pos = input.getPosition();

		ParsedResult predictResult = this.getExpr().parse(context);
		if(!(predictResult instanceof Failure)) {
			input.setPosition(pos);
			return ParsedResult.EMPTY_RESULT;
		}
		return inAnd(input, this);
	}
}