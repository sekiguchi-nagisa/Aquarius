package aquarius.bootstrap;

import java.util.ArrayList;
import java.util.List;

import aquarius.runtime.ParsingActionSet;

public abstract class ParsingExpression {
	public abstract <T> T accept(ExpressionVisitor<T> visitor);
}

// ###################################################
// ##     definition of parsing expression type     ##
// ###################################################

/**
 * try to match string literal. return matched string
 * -> 'literal'
 * @author skgchxngsxyz-opensuse
 *
 */
class string extends ParsingExpression {
	private final String target;

	private string(String target) {
		this.target = target;
	}

	public String getTarget() {
		return this.target;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitString(this);
	}
}

/**
 * try to match any one character. return mached character
 * -> .
 * @author skgchxngsxyz-opensuse
 *
 */
class any extends ParsingExpression {
	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitAny(this);
	}
}

/**
 * try to match one character from char set. return matched character
 * -> [12a-c]   1, 2, a, b, c
 * @author skgchxngsxyz-opensuse
 *
 */
class charSet extends ParsingExpression {
	private List<Integer> charList;
	private List<Pair<Integer, Integer>> rangeList;

	private charSet(int ...chars) {
		this.charList = new ArrayList<>(chars.length);
		for(int ch : chars) {
			this.charList.add(ch);
		}
	}

	@SafeVarargs
	private charSet(Pair<Integer, Integer> ...ranges) {
		this.rangeList = new ArrayList<>(ranges.length);
		for(Pair<Integer, Integer> range : ranges) {
			this.rangeList.add(range);
		}
	}

	public charSet chars(int... chars) {
		if(this.charList == null) {
			this.charList = new ArrayList<>(chars.length);
		}
		for(int ch : chars) {
			this.charList.add(ch);
		}
		return this;
	}

	@SafeVarargs
	public final charSet ranges(Pair<Integer, Integer>... ranges) {
		if(this.rangeList == null) {
			this.rangeList = new ArrayList<>(ranges.length);
		}
		for(Pair<Integer, Integer> range : ranges) {
			this.rangeList.add(range);
		}
		return this;
	}

	public List<Integer> getCharList() {
		return this.charList;
	}

	public List<Pair<Integer, Integer>> getRangeList() {
		return this.rangeList;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitCharSet(this);
	}
	
}

/**
 * try to match rule. return matched result
 * -> rule
 * @author skgchxngsxyz-opensuse
 *
 */
class rule extends ParsingExpression {

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitRule(this);
	}
	
}

abstract class compoundExpr extends ParsingExpression {
	protected final ParsingExpression expr;

	protected compoundExpr(ParsingExpression expr) {
		this.expr = expr;
	}

	public final ParsingExpression getExpr() {
		return this.expr;
	}
}


/**
 * try to match sub expression. return matched result
 * -> ( expression )
 * @author skgchxngsxyz-opensuse
 *
 */
class subExpr extends compoundExpr {
	private subExpr(ParsingExpression expr) {
		super(expr);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitSubExpr(this);
	}
}

/**
 * match zero or more repetition of the expression. return matched results as array
 * -> expr *
 * @author skgchxngsxyz-opensuse
 *
 */
class zeroMore extends compoundExpr {
	private zeroMore(ParsingExpression expr) {
		super(expr);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitZeroMore(this);
	}
}

/**
 * match one or more repetition of the expression. return matched results as array
 * -> expr +
 * @author skgchxngsxyz-opensuse
 *
 */
class oneMore extends compoundExpr {
	private oneMore(ParsingExpression expr) {
		super(expr);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitOneMore(this);
	}
}

/**
 * try to match the expression. return matched result or null
 * -> expr ?
 * @author skgchxngsxyz-opensuse
 *
 */
class optional extends compoundExpr {
	private optional(ParsingExpression expr) {
		super(expr);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitOptional(this);
	}
}

/**
 * try to match the expression. if success, not advance parsing position.
 * not return value.
 * otherwise, match failed
 * -> & expr 
 * @author skgchxngsxyz-opensuse
 *
 */
class andPredict extends compoundExpr {
	private andPredict(ParsingExpression expr) {
		super(expr);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitAndPredict(this);
	}
}

/**
 * try to match the expression. if not success, not advance parsing position.
 * otherwise, match failed. not return value.
 * -> ! expr
 * @author skgchxngsxyz-opensuse
 *
 */
class notPredict extends compoundExpr {
	private notPredict(ParsingExpression expr) {
		super(expr);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitNotPredict(this);
	}
}

abstract class listExpr extends ParsingExpression {
	protected final List<ParsingExpression> exprList;

	protected listExpr(ParsingExpression... exprs) {
		this.exprList = new ArrayList<>(exprs.length);
		for(ParsingExpression expr : exprs) {
			this.exprList.add(expr);
		}
	}

	public final List<ParsingExpression> getExprList() {
		return this.exprList;
	}
}

/**
 * try to match the sequence of expressions and return matched results as array.
 * -> expr1 expr2 ... exprN
 * @author skgchxngsxyz-opensuse
 *
 */
class seq extends listExpr {
	private seq(ParsingExpression... exprs) {
		super(exprs);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitSeq(this);
	}
}

/**
 * try to match first expression and if failed try the second one ...
 * return matched result.
 * -> expr1 / expr2 / ... / exprN
 * @author skgchxngsxyz-opensuse
 *
 */
class choice extends listExpr {
	private choice(ParsingExpression ...exprs) {
		super(exprs);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitChoice(this);
	}
}


// ##############################################
// ##     extended expression for Aquarius     ##
// ##############################################

abstract class abstractAction extends ParsingExpression {
	protected final ParsingActionSet actionSet;
	protected final int index;

	protected abstractAction(ParsingActionSet actionSet, int index) {
		this.actionSet = actionSet;
		this.index = index;
	}

	public final ParsingActionSet getActionSet() {
		return this.actionSet;
	}

	public final int getIndex() {
		return this.index;
	}
}

/**
 * try to match the expression, if success execute action. preceding expression result
 * is treated as the argument of action. return the result of action.
 * -> expr { action id }
 * @author skgchxngsxyz-opensuse
 *
 */
class action extends abstractAction {	// extended expression type
	protected action(ParsingActionSet actionSet, int index) {
		super(actionSet, index);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitAction(this);
	}
}

/**
 * execute semantic action. if return value is not failed, not advance parsing position.
 * otherwise, match failed. not return value
 * -> & { action id }
 * @author skgchxngsxyz-opensuse
 *
 */
class andPredictAction extends abstractAction {	// extended expression type
	private andPredictAction(ParsingActionSet actionSet, int index) {
		super(actionSet, index);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitAndPredictAction(this);
	}
}

/**
 * execute semantic action. if return value is failed, not advance parsing position.
 * otherwise, match failed. mpt return value
 * -> ! { action id }
 * @author skgchxngsxyz-opensuse
 *
 */
class notPredictAction extends abstractAction {	// extended expression type
	private notPredictAction(ParsingActionSet actionSet, int index) {
		super(actionSet, index);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitNotPredictAction(this);
	}
}

/**
 * try to match sub expression and return matched result as one string.
 * -> < expr >
 * @author skgchxngsxyz-opensuse
 *
 */
class capture extends compoundExpr {	// extended expression type
	private capture(ParsingExpression expr) {
		super(expr);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitCapture(this);
	}
}
