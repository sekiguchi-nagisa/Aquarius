/*
 * Copyright (C) 2014-2015 Nagisa Sekiguchi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *//*
 * Copyright (C) 2015 Nagisa Sekiguchi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package aquarius.expression;

import java.util.ArrayList;
import java.util.List;

import static aquarius.misc.Utf8Util.*;

import aquarius.AquariusInputStream;
import aquarius.ExpressionVisitor;
import aquarius.ParserContext;
import aquarius.misc.IntRange;

/**
* try to match one utf8 character from char set.
* -> [12a-c]   1, 2, a, b, c
* @author skgchxngsxyz-opensuse
*
*/
public class CharSet implements ParsingExpression<Void> {
	protected final int[] chars;
	protected List<IntRange> rangeList;

	public static CharSet newCharSet(char... chars) {
		int[] codes = new int[chars.length];
		for(int i = 0; i < chars.length; i++) {
			codes[i] = toUtf8Code(chars[i]);
		}
		return newCharSet(codes);
	}

	public static CharSet newCharSet(int... chars) {
		for(int ch : chars) {
			if(!isUtf8Code(ch)) {
				throw new IllegalArgumentException("must be utf8 code: " + Integer.toHexString(ch));
			}
			if(!isAsciiCode(ch)) {
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
		return this.r(toUtf8Code(start), toUtf8Code(stop));
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
	 * if start or stop is not utf8 code
	 * if start >= stop
	 */
	public CharSet r(int start, int stop) throws IllegalArgumentException {
		if(!isUtf8Code(start)) {
			throw new IllegalArgumentException("must be utf8 code: " + Integer.toHexString(start));
		}
		if(!isUtf8Code(stop)) {
			throw new IllegalArgumentException("must be utf8 code: " + Integer.toHexString(stop));
		}
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