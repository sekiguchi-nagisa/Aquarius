package aquarius.combinator.expression;

import java.util.ArrayList;
import java.util.List;

import aquarius.combinator.ExpressionVisitor;
import aquarius.util.IntPair;

/**
* try to match one character from char set. return matched character
* -> [12a-c]   1, 2, a, b, c
* @author skgchxngsxyz-opensuse
*
*/
public class CharSet implements ParsingExpression {
	private List<Integer> charList;
	private List<IntPair> rangeList;

	public CharSet(int ...chars) {
		this.charList = new ArrayList<>(chars.length);
		for(int ch : chars) {
			this.charList.add(ch);
		}
	}

	public CharSet ranges(int start, int stop) {
		if(this.rangeList == null) {
			this.rangeList = new ArrayList<>();
		}
		this.rangeList.add(new IntPair(start, stop));
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
	public List<IntPair> getRangeList() {
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
			for(IntPair range : this.rangeList) {
				sBuilder.append((char) range.getLeft());
				sBuilder.append('-');
				sBuilder.append((char) range.getRight());
			}
		}
		sBuilder.append(']');
		return sBuilder.toString();
	}
}