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
 * @param <R>
*
*/
public class AndPredict<R> implements ParsingExpression<R> {
	private final ParsingExpression<R> precedingExpr;
	private final ParsingExpression<?> predictiveExpr;

	public AndPredict(ParsingExpression<R> precedingExpr, ParsingExpression<?> predictiveExpr) {
		this.precedingExpr = precedingExpr;
		this.predictiveExpr = predictiveExpr;
	}

	public ParsingExpression<R> getPrecedingExpr() {
		return this.precedingExpr;
	}

	public ParsingExpression<?> getPredictiveExpr() {
		return this.predictiveExpr;
	}

	@Override
	public String toString() {
		return "&" + this.predictiveExpr.toString();
	}

	@Override
	public Result<R> parse(ParserContext context) {
		// parse preceding expression
		int precedingPos = context.getInputStream().getPosition();
		Result<R> precedingResult = this.precedingExpr.parse(context);
		if(precedingResult.isFailure()) {
			return precedingResult;
		}

		// parse prediction
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();

		Result<?> predictResult = this.predictiveExpr.parse(context);
		if(!(predictResult.isFailure())) {
			/**
			 * if prediction is success, return preceding result 
			 */
			input.setPosition(pos);
			return precedingResult;
		}
		try {
			return inAnd(pos, this);
		} finally {
			input.setPosition(precedingPos);
		}
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitAndPredict(this);
	}
}