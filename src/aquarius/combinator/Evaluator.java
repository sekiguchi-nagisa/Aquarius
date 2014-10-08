package aquarius.combinator;

import aquarius.combinator.expression.Action;
import aquarius.combinator.expression.AndPredict;
import aquarius.combinator.expression.AndPredictAction;
import aquarius.combinator.expression.Any;
import aquarius.combinator.expression.Capture;
import aquarius.combinator.expression.CharSet;
import aquarius.combinator.expression.Choice;
import aquarius.combinator.expression.NotPredict;
import aquarius.combinator.expression.NotPredictAction;
import aquarius.combinator.expression.OneMore;
import aquarius.combinator.expression.Optional;
import aquarius.combinator.expression.Rule;
import aquarius.combinator.expression.Sequence;
import aquarius.combinator.expression.StringLiteral;
import aquarius.combinator.expression.SubExpr;
import aquarius.combinator.expression.ZeroMore;
import aquarius.runtime.BaseParser;
import aquarius.runtime.ParsedResult;

public class Evaluator extends BaseParser implements ExpressionVisitor<ParsedResult> {
	private final Rule[] rules;

	public Evaluator(Rule[] rules) {
		this.rules = rules;
	}

	@Override
	public ParsedResult visitString(StringLiteral expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParsedResult visitAny(Any expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParsedResult visitCharSet(CharSet expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParsedResult visitRule(Rule expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParsedResult visitSubExpr(SubExpr expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParsedResult visitZeroMore(ZeroMore expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParsedResult visitOneMore(OneMore expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParsedResult visitOptional(Optional expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParsedResult visitAndPredict(AndPredict expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParsedResult visitNotPredict(NotPredict expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParsedResult visitSeq(Sequence expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParsedResult visitChoice(Choice expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParsedResult visitAction(Action expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParsedResult visitAndPredictAction(AndPredictAction expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParsedResult visitNotPredictAction(NotPredictAction expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParsedResult visitCapture(Capture expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRuleSize() {
		return this.rules.length;
	}

	@Override
	protected ParsedResult dispatchRule(int ruleIndex) throws IndexOutOfBoundsException {
		int srcPos = this.input.getPosition();
		ParsedResult result = this.memoTable.get(ruleIndex, srcPos);
		if(result != null) {
			return result;
		}
		return this.memoTable.set(ruleIndex, srcPos, this.rules[ruleIndex].getPattern().accept(this));
	}

}
