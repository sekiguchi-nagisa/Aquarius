package aquarius.matcher.expression;

import java.util.ArrayList;
import java.util.List;

import aquarius.matcher.ExpressionVisitor;
import aquarius.matcher.ParserContext;
import aquarius.misc.IntRange;
import aquarius.runtime.AquariusInputStream;
import aquarius.runtime.Result;
import static aquarius.runtime.Result.*;
import aquarius.runtime.Token;

/**
* try to match one character from char set. return matched character
* -> [12a-c]   1, 2, a, b, c
* @author skgchxngsxyz-opensuse
*
*/
public class CharSet implements ParsingExpression<Token> {
	private final int[] chars;
	private List<IntRange> rangeList;

	/**
	 * 
	 * @param chars
	 * must be utf8 character
	 */
	public CharSet(int ...chars) {
		this.chars = chars;
	}

	/**
	 * add char range
	 * @param start
	 * inclusive. must be utf8 character.
	 * @param stop
	 * inclusive. must be utf8 character.
	 * @return
	 * this
	 * @throws IllegalArgumentException
	 * if start >= stop
	 */
	public CharSet r(int start, int stop) throws IllegalArgumentException {
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

	@Override
	public Result<Token> parse(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		int pos = input.getPosition();

		if(pos == input.getInputSize()) {
			return inEOF(input, this);
		}

		final int fetchedCh = input.fetch();
		// match chars
		for(int ch : this.getChars()) {
			if(fetchedCh == ch) {
				input.consume();
				return of(input.createToken(pos));
			}
		}
		// match char range
		List<IntRange> rangeList = this.getRangeList();
		if(rangeList != null) {
			for(IntRange range : rangeList) {
				if(range.withinRange(fetchedCh)) {
					input.consume();
					return of(input.createToken(pos));
				}
			}
		}
		return inCharSet(input, this, fetchedCh); //FIXME:
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitCharSet(this);
	}
}