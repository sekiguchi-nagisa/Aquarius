package aquarius.bootstrap;

import java.util.ArrayList;
import java.util.List;

import aquarius.runtime.ParsedResult;
import aquarius.runtime.ParsingActionSet;

public abstract class ParsingExpression implements ParsedResult {
	public abstract <T> T accept(ExpressionVisitor<T> visitor);

	/**
	 * 
	 * @param start
	 * inclusive
	 * @param stop
	 * inclusive
	 * @return
	 */
	public static Pair<Integer, Integer> range(int start, int stop) {
		return new Pair<Integer, Integer>(start, stop);
	}


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

	@SafeVarargs
	public static CharSet ch(Pair<Integer, Integer> ...ranges) {
		return new CharSet(ranges);
	}

	public static Rule rule(String ruleName, ParsingExpression expr) {
		return new Rule(ruleName, expr);
	}

	public static Rule rule(String ruleName) {
		return new Rule(ruleName, null);
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

	public static Action action(ParsingExpression expr, int index) {
		return new Action(expr, index);
	}

	public static AndPredictAction andAction(int index) {
		return new AndPredictAction(index);
	}

	public static NotPredictAction notAction(int index) {
		return new NotPredictAction(index);
	}

	public static Capture capture(ParsingExpression... exprs) {
		return new Capture(exprs);
	}
}


//###################################################
//##     definition of parsing expression type     ##
//###################################################

/**
* try to match string literal. return matched string
* -> 'literal'
* @author skgchxngsxyz-opensuse
*
*/
class StringLiteral extends ParsingExpression {
	private final String target;

	StringLiteral(String target) {
		this.target = target;
	}

	public String getTarget() {
		return this.target;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitString(this);
	}

	@Override
	public String toString() {
		return "'" + this.target + "'";
	}
}

/**
* try to match any one character. return mached character
* -> .
* @author skgchxngsxyz-opensuse
*
*/
class Any extends ParsingExpression {
	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitAny(this);
	}

	@Override
	public String toString() {
		return ".";
	}
}

/**
* try to match one character from char set. return matched character
* -> [12a-c]   1, 2, a, b, c
* @author skgchxngsxyz-opensuse
*
*/
class CharSet extends ParsingExpression {
	private List<Integer> charList;
	private List<Pair<Integer, Integer>> rangeList;

	CharSet(int ...chars) {
		this.charList = new ArrayList<>(chars.length);
		for(int ch : chars) {
			this.charList.add(ch);
		}
	}

	@SafeVarargs
	CharSet(Pair<Integer, Integer> ...ranges) {
		this.rangeList = new ArrayList<>(ranges.length);
		for(Pair<Integer, Integer> range : ranges) {
			this.rangeList.add(range);
		}
	}

	public CharSet chars(int... chars) {
		if(this.charList == null) {
			this.charList = new ArrayList<>(chars.length);
		}
		for(int ch : chars) {
			this.charList.add(ch);
		}
		return this;
	}

	@SafeVarargs
	public final CharSet ranges(Pair<Integer, Integer>... ranges) {
		if(this.rangeList == null) {
			this.rangeList = new ArrayList<>(ranges.length);
		}
		for(Pair<Integer, Integer> range : ranges) {
			this.rangeList.add(range);
		}
		return this;
	}

	/**
	 * 
	 * @return
	 * may be null
	 */
	public List<Integer> getCharList() {
		return this.charList;
	}

	/**
	 * 
	 * @return
	 * may be null
	 */
	public List<Pair<Integer, Integer>> getRangeList() {
		return this.rangeList;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitCharSet(this);
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append('[');
		if(this.charList != null) {
			for(int ch : this.charList) {
				sBuilder.append((char) ch);
			}
		}
		if(this.rangeList != null) {
			for(Pair<Integer, Integer> range : this.rangeList) {
				sBuilder.append((char) range.getLeft().intValue());
				sBuilder.append('-');
				sBuilder.append((char) range.getRight().intValue());
			}
		}
		sBuilder.append(']');
		return sBuilder.toString();
	}
}

/**
* try to match rule. return matched result
* -> rule
* @author skgchxngsxyz-opensuse
*
*/
class Rule extends ParsingExpression {
	private final String ruleName;
	private ParsingExpression pattern;
	private ParsingActionSet actionSet;

	/**
	 * 
	 * @param pattern
	 * may be null
	 */
	Rule(String ruleName, ParsingExpression pattern) {
		this.ruleName = ruleName;
		this.pattern = pattern;
	}

	public String getRuleName() {
		return this.ruleName;
	}

	/**
	 * 
	 * @return
	 * may be null
	 */
	public ParsingExpression getPattern() {
		return this.pattern;
	}

	/**
	 * 
	 * @param pattern
	 * not null
	 * @return
	 */
	public Rule of(ParsingExpression pattern) {
		this.pattern = pattern;
		return this;
	}

	/**
	 * not null
	 * @param actionSet
	 */
	public void setActionSet(ParsingActionSet actionSet) {
		this.actionSet = actionSet;
	}

	/**
	 * 
	 * @return
	 * may be null
	 */
	public ParsingActionSet getActionSet() {
		return this.actionSet;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitRule(this);
	}

	@Override
	public String toString() {
		return this.ruleName;
	}

	public String stringify() {
		if(this.pattern == null) {
			return this.ruleName + " = $null$";
		}
		return this.ruleName + " = " + this.pattern.toString() + ";";
	}
}

abstract class CompoundExpr extends ParsingExpression {
	protected final ParsingExpression expr;

	protected CompoundExpr(ParsingExpression expr) {
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
class SubExpr extends CompoundExpr {
	SubExpr(ParsingExpression expr) {
		super(expr);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitSubExpr(this);
	}

	@Override
	public String toString() {
		return "(" + this.expr.toString() + ")";
	}
}

/**
* match zero or more repetition of the expression. return matched results as array
* -> expr *
* @author skgchxngsxyz-opensuse
*
*/
class ZeroMore extends CompoundExpr {
	ZeroMore(ParsingExpression expr) {
		super(expr);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitZeroMore(this);
	}

	@Override
	public String toString() {
		return this.expr.toString() + "*";
	}
}

/**
* match one or more repetition of the expression. return matched results as array
* -> expr +
* @author skgchxngsxyz-opensuse
*
*/
class OneMore extends CompoundExpr {
	OneMore(ParsingExpression expr) {
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
}

/**
* try to match the expression. return matched result or null
* -> expr ?
* @author skgchxngsxyz-opensuse
*
*/
class Optional extends CompoundExpr {
	Optional(ParsingExpression expr) {
		super(expr);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitOptional(this);
	}

	@Override
	public String toString() {
		return this.expr.toString() + "?";
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
class AndPredict extends CompoundExpr {
	AndPredict(ParsingExpression expr) {
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
}

/**
* try to match the expression. if not success, not advance parsing position.
* otherwise, match failed. not return value.
* -> ! expr
* @author skgchxngsxyz-opensuse
*
*/
class NotPredict extends CompoundExpr {
	NotPredict(ParsingExpression expr) {
		super(expr);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitNotPredict(this);
	}

	@Override
	public String toString() {
		return "!" + this.expr.toString();
	}
}

abstract class ListExpr extends ParsingExpression {
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

/**
* try to match the sequence of expressions and return matched results as array.
* -> expr1 expr2 ... exprN
* @author skgchxngsxyz-opensuse
*
*/
class Sequence extends ListExpr {
	Sequence(ParsingExpression... exprs) {
		super(exprs);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitSeq(this);
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		final int size = this.exprList.size();
		for(int i = 0; i < size; i++) {
			if(i > 0) {
				sBuilder.append(' ');
			}
			sBuilder.append(this.exprList.get(i));
		}
		return sBuilder.toString();
	}
}

/**
* try to match first expression and if failed try the second one ...
* return matched result.
* -> expr1 / expr2 / ... / exprN
* @author skgchxngsxyz-opensuse
*
*/
class Choice extends ListExpr {
	Choice(ParsingExpression ...exprs) {
		super(exprs);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitChoice(this);
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		final int size = this.exprList.size();
		for(int i = 0; i < size; i++) {
			if(i > 0) {
				sBuilder.append(" / ");
			}
			sBuilder.append(this.exprList.get(i));
		}
		return sBuilder.toString();
	}
}


//##############################################
//##     extended expression for Aquarius     ##
//##############################################

abstract class AbstractAction extends ParsingExpression {
	protected final int index;

	protected AbstractAction(int index) {
		this.index = index;
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
class Action extends AbstractAction {	// extended expression type
	private final ParsingExpression expr;

	Action(ParsingExpression expr, int index) {
		super(index);
		this.expr = expr;
	}

	public ParsingExpression getExpr() {
		return this.expr;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitAction(this);
	}

	@Override
	public String toString() {
		return this.expr + " {" + this.index + "}";
	}
}

/**
* execute semantic action. if return value is not failed, not advance parsing position.
* otherwise, match failed. not return value
* -> & { action id }
* @author skgchxngsxyz-opensuse
*
*/
class AndPredictAction extends AbstractAction {	// extended expression type
	AndPredictAction(int index) {
		super(index);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitAndPredictAction(this);
	}

	@Override
	public String toString() {
		return "&{" + this.index + "}";
	}
}

/**
* execute semantic action. if return value is failed, not advance parsing position.
* otherwise, match failed. mpt return value
* -> ! { action id }
* @author skgchxngsxyz-opensuse
*
*/
class NotPredictAction extends AbstractAction {	// extended expression type
	NotPredictAction(int index) {
		super(index);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitNotPredictAction(this);
	}

	@Override
	public String toString() {
		return "!{" + this.index + "}";
	}
}

/**
* try to match sub expression sequence and return matched result as one string.
* -> < expr1 expr2 ... exprN >
* @author skgchxngsxyz-opensuse
*
*/
class Capture extends ListExpr {	// extended expression type
	Capture(ParsingExpression... exprs) {
		super(exprs);
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitCapture(this);
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append('<');
		final int size = this.exprList.size();
		for(int i = 0; i < size; i++) {
			if(i > 0) {
				sBuilder.append(' ');
			}
			sBuilder.append(this.exprList.get(i));
		}
		sBuilder.append('>');
		return sBuilder.toString();
	}
}