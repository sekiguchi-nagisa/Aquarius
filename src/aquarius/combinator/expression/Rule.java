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

	public ParsingExpression getPattern();

	/**
	 * get unique identifier
	 * @return
	 * non negative value
	 */
	public int getRuleIndex();

	/**
	 * must not override
	 */
	public default <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitRule(this);
	}
}