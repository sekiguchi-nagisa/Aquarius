package aquarius.matcher.expression;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.ParserContext;
import aquarius.runtime.AquariusInputStream;
import aquarius.runtime.Result;
import static aquarius.runtime.Result.*;

/**
* try to match the expression. if success, not advance parsing position.
* not return value.
* otherwise, match failed
* -> & expr 
* @author skgchxngsxyz-opensuse
*
*/
public class AndPredict implements ParsingExpression<Void> {
	private final ParsingExpression<?> expr;

	public AndPredict(ParsingExpression<?> expr) {
		this.expr = expr;
	}

	public ParsingExpression<?> getExpr() {
		return this.expr;
	}

	@Override
	public String toString() {
		return "&" + this.expr.toString();
	}

	@Override
	public Result<Void> parse(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();

		Result<?> predictResult = this.getExpr().parse(context);
		if(!(predictResult.isFailure())) {
			input.setPosition(pos);
			return empty();
		}
		try {
			return inAnd(pos, this);
		} finally {
			input.setPosition(pos);
		}
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitAndPredict(this);
	}
}