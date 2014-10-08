package aquarius.runtime.expression;

import aquarius.runtime.ExpressionVisitor;

/**
* try to match rule. return matched result
* -> rule
* @author skgchxngsxyz-opensuse
*
*/
public class Rule implements ParsingExpression {
	private final String ruleName;
	private ParsingExpression pattern;

	/**
	 * 
	 * @param pattern
	 * may be null
	 */
	public Rule(String ruleName, ParsingExpression pattern) {
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