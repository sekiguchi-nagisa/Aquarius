package aquarius.combinator.expression;

import aquarius.combinator.ExpressionVisitor;

/**
* try to match rule. return matched result
* -> rule
* @author skgchxngsxyz-opensuse
*
*/
public interface Rule extends ParsingExpression {
	public String getRuleName();

	/**
	 * initialize inner expression
	 */
	public void init();

	/**
	 * may be null
	 * @return
	 */
	public ParsingExpression getPattern();

	public int getRuleIndex();

	/**
	 * must not override
	 */
	public default <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitRule(this);
	}
}