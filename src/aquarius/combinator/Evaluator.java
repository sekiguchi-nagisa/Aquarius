package aquarius.combinator;

import java.util.ArrayList;
import java.util.List;

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
import aquarius.combinator.expression.ParsingExpression;
import aquarius.combinator.expression.Rule;
import aquarius.combinator.expression.Sequence;
import aquarius.combinator.expression.StringLiteral;
import aquarius.combinator.expression.SubExpr;
import aquarius.combinator.expression.ZeroMore;
import aquarius.runtime.AquariusInputStream;
import aquarius.runtime.BaseParser;
import aquarius.runtime.Failure;
import aquarius.runtime.ParsedResult;
import aquarius.runtime.ResultList;
import aquarius.runtime.ParsedResult.EmptyResult;
import aquarius.util.IntRange;

public class Evaluator extends BaseParser implements ExpressionVisitor<ParsedResult> {
	private final Rule[] rules;

	public Evaluator(Rule[] rules) {
		this.rules = rules;
		for(Rule rule : rules) {
			rule.init();	// initialize rule
		}
	}

	@Override
	public ParsedResult visitString(StringLiteral expr) {
		int pos = this.input.getPosition();

		String text = expr.getTarget();
		final int size = text.length();
		for(int i = 0; i < size; i++) {
			if(text.charAt(i) != this.input.consume()) {
				return new Failure(this.input.getPosition(), "require string: " + text + 
						", but is " + this.input.createToken(pos, this.input.getPosition()));	//TODO:
			}
		}
		return this.input.createToken(pos);
	}

	@Override
	public ParsedResult visitAny(Any expr) {
		int pos = this.input.getPosition();

		if(this.input.consume() == AquariusInputStream.EOF) {
			return new Failure(this.input.getPosition(), "reach End of File");	//TODO:
		}
		return this.input.createToken(pos);
	}

	@Override
	public ParsedResult visitCharSet(CharSet expr) {
		int pos = this.input.getPosition();

		final int fetchedCh = this.input.consume();
		if(fetchedCh == AquariusInputStream.EOF) {
			return new Failure(this.input.getPosition(), "reach End of File");
		}

		// match chars
		for(int ch : expr.getChars()) {
			if(fetchedCh == ch) {
				return this.input.createToken(pos);
			}
		}
		// match char range
		List<IntRange> rangeList = expr.getRangeList();
		if(rangeList != null) {
			for(IntRange range : rangeList) {
				if(range.withinRange(fetchedCh)) {
					return this.input.createToken(pos);
				}
			}
		}
		return new Failure(this.input.getPosition(), 
				"current char is " + (char) fetchedCh + ", but require chars: " + expr);	//TODO:
	}

	@Override
	public ParsedResult visitRule(Rule expr) {
		return this.dispatchRule(expr.getRuleIndex());
	}

	@Override
	public ParsedResult visitSubExpr(SubExpr expr) {
		return expr.getExpr().accept(this);
	}

	@Override
	public ParsedResult visitZeroMore(ZeroMore expr) {
		ResultList list = new ResultList();
		while(true) {
			int pos = this.input.getPosition();

			ParsedResult result = expr.getExpr().accept(this);
			if(result instanceof Failure) {
				this.input.setPosition(pos);	// roll back position
				break;
			}
			list.add(result);
		}
		return list.isEmpty() ? ParsedResult.NULL_RESULT : list;
	}

	@Override
	public ParsedResult visitOneMore(OneMore expr) {
		ResultList list = new ResultList();
		while(true) {
			int pos = this.input.getPosition();

			ParsedResult result = expr.getExpr().accept(this);
			if(result instanceof Failure) {
				if(list.isEmpty()) {
					return new Failure(this.input.getPosition(), 
							"require at least one pattern: " + expr.getExpr());	//TODO:
				}
				this.input.setPosition(pos);	// roll back position
				break;
			}
			list.add(result);
		}
		return list;
	}

	@Override
	public ParsedResult visitOptional(Optional expr) {
		int pos = this.input.getPosition();

		ParsedResult result = expr.getExpr().accept(this);
		if(result instanceof Failure) {
			this.input.setPosition(pos);	// roll back position
			return ParsedResult.NULL_RESULT;
		}
		return result;
	}

	@Override
	public ParsedResult visitAndPredict(AndPredict expr) {
		int pos = this.input.getPosition();

		ParsedResult predictResult = expr.getExpr().accept(this);
		if(!(predictResult instanceof Failure)) {
			this.input.setPosition(pos);
			return ParsedResult.EMPTY_RESULT;
		}
		return new Failure(this.input.getPosition(), 
				"and prediction failed: " + expr.getExpr());	//TODO:
	}

	@Override
	public ParsedResult visitNotPredict(NotPredict expr) {
		int pos = this.input.getPosition();

		ParsedResult predictResult = expr.getExpr().accept(this);
		if(predictResult instanceof Failure) {
			this.input.setPosition(pos);
			return ParsedResult.EMPTY_RESULT;
		}
		return new Failure(this.input.getPosition(), 
				"not prediction failed: " + expr.getExpr());	//TODO:
	}

	@Override
	public ParsedResult visitSeq(Sequence expr) {
		ResultList list = new ResultList(expr.getExprList().size());
		for(ParsingExpression e : expr.getExprList()) {
			ParsedResult result = e.accept(this);
			if(result instanceof Failure) {
				return result;
			}
			if(result instanceof EmptyResult) {
				continue;	// skip EmptyResult
			}
			list.add(result);
		}
		return list;
	}

	@Override
	public ParsedResult visitChoice(Choice expr) {
		// for error report
		List<Failure> failures = new ArrayList<>(expr.getExprList().size());

		int pos = this.input.getPosition();
		for(ParsingExpression e : expr.getExprList()) {
			ParsedResult result = e.accept(this);
			if(result instanceof Failure) {
				failures.add((Failure) result);
				this.input.setPosition(pos);	// roll back position
				continue;
			}
			return result;
		}
		Failure longestMatched = failures.get(0);
		for(Failure failure : failures) {
			if(failure.getFailurePos() > longestMatched.getFailurePos()) {
				longestMatched = failure;
			}
		}
		return longestMatched;	// longe
	}

	@Override
	public ParsedResult visitAction(Action expr) {
		// evaluate preceding expression
		ParsedResult result = expr.getExpr().accept(this);
		if(result instanceof Failure) {
			return result;
		}

		// invoke action
		return expr.getAction().invoke(result);	// may be Failure
	}

	@Override
	public ParsedResult visitAndPredictAction(AndPredictAction expr) {
		throw new RuntimeException("unsuppored: " + expr.getClass());	//TODO:
	}

	@Override
	public ParsedResult visitNotPredictAction(NotPredictAction expr) {
		throw new RuntimeException("unsuppored: " + expr.getClass());	//TODO:
	}

	@Override
	public ParsedResult visitCapture(Capture expr) {
		int pos = this.input.getPosition();
		for(ParsingExpression e : expr.getExprList()) {
			ParsedResult result = e.accept(this);
			if(result instanceof Failure) {
				return result;
			}
		}
		return this.input.createToken(pos);
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
		// if not found previous parsed result, invoke rule
		result = this.rules[ruleIndex].getPattern().accept(this);
		if(result instanceof Failure) {
			return result;
		}
		return this.memoTable.set(ruleIndex, srcPos, result);
	}

}
