package aquarius.combinator.expression;

import java.util.ArrayList;
import java.util.List;

import aquarius.combinator.ExpressionVisitor;
import aquarius.combinator.ParsingAction;
import aquarius.combinator.PredictiveAction;
import aquarius.runtime.ParsedResult;

public interface ParsingExpression extends ParsedResult {
	public <T> T accept(ExpressionVisitor<T> visitor);

	// creator api
	public static StringLiteral string(String target) {
		return new StringLiteral(target);
	}

	public static Any any() {
		return new Any();
	}

	public static CharSet ch(int ...chars) {
		return new CharSet(chars);
	}

	public static SubExpr sub(ParsingExpression expr) {
		return new SubExpr(expr);
	}

	public static ZeroMore zeroMore(ParsingExpression expr) {
		return new ZeroMore(expr);
	}

	public static OneMore oneMore(ParsingExpression expr) {
		return new OneMore(expr);
	}

	public static Optional opt(ParsingExpression expr) {
		return new Optional(expr);
	}

	public static AndPredict and(ParsingExpression expr) {
		return new AndPredict(expr);
	}

	public static NotPredict not(ParsingExpression expr) {
		return new NotPredict(expr);
	}

	public static Sequence seq(ParsingExpression... exprs) {
		return new Sequence(exprs);
	}

	public static Choice choice(ParsingExpression... exprs) {
		return new Choice(exprs);
	}

	public static Action action(ParsingExpression expr, ParsingAction action) {
		return new Action(expr, action);
	}

	public static AndPredictAction andAction(PredictiveAction action) {
		return new AndPredictAction(action);
	}

	public static NotPredictAction notAction(PredictiveAction action) {
		return new NotPredictAction(action);
	}

	public static Capture capture(ParsingExpression... exprs) {
		return new Capture(exprs);
	}
}


abstract class CompoundExpr implements ParsingExpression {
	protected final ParsingExpression expr;

	protected CompoundExpr(ParsingExpression expr) {
		this.expr = expr;
	}

	public final ParsingExpression getExpr() {
		return this.expr;
	}
}


abstract class ListExpr implements ParsingExpression {
	protected final List<ParsingExpression> exprList;

	protected ListExpr(ParsingExpression... exprs) {
		this.exprList = new ArrayList<>(exprs.length);
		for(ParsingExpression expr : exprs) {
			this.exprList.add(expr);
		}
	}

	public final List<ParsingExpression> getExprList() {
		return this.exprList;
	}
}

