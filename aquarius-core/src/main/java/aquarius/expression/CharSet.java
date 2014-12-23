package aquarius.expression;

import java.util.ArrayList;
import java.util.List;

import aquarius.AquariusInputStream;
import aquarius.ExpressionVisitor;
import aquarius.ParserContext;
import aquarius.misc.IntRange;
import aquarius.misc.Utf8Util;

/**
* try to match one utf8 character from char set.
* -> [12a-c]   1, 2, a, b, c
* @author skgchxngsxyz-opensuse
*
*/
public class CharSet implements ParsingExpression<Void> {
	protected final int[] chars;
	protected List<IntRange> rangeList;

	private static int[] toUtf8Chars(char[] chars) {
		int[] encoddedChars = new int[chars.length];
		for(int i = 0; i < chars.length; i++) {
			encoddedChars[i] = Utf8Util.toUtf8Code(chars[i]);
		}
		return encoddedChars;
	}

	public static CharSet newCharSet(char... chars) {
		return newCharSet(toUtf8Chars(chars));
	}

	public static CharSet newCharSet(int... chars) {
		for(int ch : chars) {
			if(!Utf8Util.isAsciiCode(ch)) {
				return new CharSet(chars);
			}
		}
		return new AsciiCharSet(chars);
	}

	/**
	 * 
	 * @param chars
	 * must be utf8 character
	 */
	public CharSet(int[] chars) {
		this.chars = chars;
	}

	/**
	 * 
	 * @param start
	 * java utf16 character
	 * @param stop
	 * java utf16 character
	 * @return
	 * this
	 */
	public CharSet r(char start, char stop) {
		return this.r(Utf8Util.toUtf8Code(start), Utf8Util.toUtf8Code(stop));
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
			switch(ch) {
			case '\t': sBuilder.append("\\t"); break;
			case '\n': sBuilder.append("\\n"); break;
			case '\r': sBuilder.append("\\r"); break;
			default:
				sBuilder.append((char) ch);
			}
			
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
	public boolean parseImpl(ParserContext context) {
		AquariusInputStream input = context.getInputStream();
		final int fetchedCh = input.fetch();
		if(fetchedCh != AquariusInputStream.EOF) {
			// match chars
			for(int ch : this.chars) {
				if(fetchedCh == ch) {
					input.consume();
					return true;
				}
			}
			// match char range
			List<IntRange> rangeList = this.getRangeList();
			if(rangeList != null) {
				for(IntRange range : rangeList) {
					if(range.withinRange(fetchedCh)) {
						input.consume();
						return true;
					}
				}
			}
		}
		context.pushFailure(input.getPosition(), this);
		return false;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitCharSet(this);
	}

	@Override
	public boolean isReturnable() {
		return false;
	}
}