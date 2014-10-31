package aquarius.matcher.expression;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.ParserContext;
import aquarius.runtime.AquariusInputStream;
import aquarius.runtime.Result;
import static aquarius.runtime.Result.*;

/**
* try to match the expression. if not success, not advance parsing position.
* otherwise, match failed. not return value.
* -> ! expr
* @author skgchxngsxyz-opensuse
 * @param <R>
*
*/
public class NotPredict<R> implements ParsingExpression<R> {
	private final ParsingExpression<R> precedingExpr;
	private final ParsingExpression<?> predictiveExpr;

	public NotPredict(ParsingExpression<R> precedingExpr, ParsingExpression<?> predictiveExpr) {
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
		return "!" + this.predictiveExpr.toString();
	}

	@Override
	public Result<R> parse(ParserContext context) {
		// parse preceding expr
		int precedingPos = context.getInputStream().getPosition();
		Result<R> precedingResult = this.precedingExpr.parse(context);
		if(precedingResult.isFailure()) {
			return precedingResult;
		}

		// parse prediction
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();

		Result<?> predictResult = this.predictiveExpr.parse(context);
		if(predictResult.isFailure()) {
			/**
			 * if prediction is failed, return preceding result
			 */
			input.setPosition(pos);
			return precedingResult;
		}
		try {
			return inNot(pos, this);
		} finally {
			input.setPosition(precedingPos);
		}
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitNotPredict(this);
	}
}