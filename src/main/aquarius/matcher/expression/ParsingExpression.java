package aquarius.matcher.expression;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.ParserContext;
import aquarius.matcher.ParsingAction;
import aquarius.matcher.PredictiveAction;
import aquarius.runtime.Result;

public interface ParsingExpression<R> {
	public <T> T accept(ExpressionVisitor<T> visitor);

	public Result<R> parse(ParserContext context);

	// for action
	public default <R2> Action<R2, R> action(ParsingAction<R, R2> action) {
		return new Action<>(this, action);
	}

	// for prediction
	public default AndPredict<R> and(ParsingExpression<?> predictiveExpr) {
		return new AndPredict<>(this, predictiveExpr);
	}

	public default NotPredict<R> not(ParsingExpression<?> predictiveExpr) {
		return new NotPredict<>(this, predictiveExpr);
	}

	// for predictive action
	public default AndPredictAction<R> and(PredictiveAction<R> action) {
		return new AndPredictAction<>(this, action);
	}

	public default NotPredictAction<R> not(PredictiveAction<R> action) {
		return new NotPredictAction<>(this, action);
	}
}
