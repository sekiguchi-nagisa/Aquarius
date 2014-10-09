package aquarius.combinator.expression;

import java.util.ArrayList;
import java.util.List;

import aquarius.combinator.ExpressionVisitor;
import aquarius.util.IntRange;

/**
* try to match one character from char set. return matched character
* -> [12a-c]   1, 2, a, b, c
* @author skgchxngsxyz-opensuse
*
*/
public class CharSet implements ParsingExpression {
	private final int[] chars;
	private List<IntRange> rangeList;

	public CharSet(int ...chars) {
		this.chars = chars;
	}

	/**
	 * add char range
	 * @param start
	 * inclusive
	 * @param stop
	 * inclusive
	 * @return
	 * this
	 * @throws IllegalArgumentException
	 * if start >= stop
	 */
	public CharSet range(int start, int stop) throws IllegalArgumentException {
		if(start >= stop) {
			throw new IllegalArgumentException(
					"start is larger than stop. start: " + start + "stop: " + stop);
		}
		if(this.rangeList == null) {
			this.rangeList = new ArrayList<>();
		}
		this.rangeList.add(new IntRange(start, stop));
		return this;
	}

	/**
	 * 
	 * @return
	 * may be null
	 */
	public int[] getChars() {
		return this.chars;
	}

	/**
	 * 
	 * @return
	 * may be null
	 */
	public List<IntRange> getRangeList() {
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
		for(int ch : this.chars) {
			sBuilder.append((char) ch);
		}
		if(this.rangeList != null) {
			for(IntRange range : this.rangeList) {
				sBuilder.append((char) range.getStart());
				sBuilder.append('-');
				sBuilder.append((char) range.getStop());
			}
		}
		sBuilder.append(']');
		return sBuilder.toString();
	}
}