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
 */

package aquarius;

/**
 * represent for token
 * @author skgchxngsxyz-opensuse
 *
 */
public class Token {
	protected int startPos;
	protected int size;

	public Token(int startPos, int size) {
		this.startPos = startPos;
		this.size = size;
	}

	/**
	 * get token start position. inclusive
	 * @return
	 * 
	 */
	public int getStartPos() {
		return this.startPos;
	}

	/**
	 * get token text size.
	 * @return
	 */
	public int getSize() {
		return this.size;
	}

	@Override
	public String toString() {
		return "token<" + this.startPos + ":" + this.size + ">";
	}

	@Override
	public boolean equals(Object target) {
		if(target instanceof Token) {
			Token token = (Token) target;
			return this.getStartPos() == token.getStartPos() && 
					this.getSize() == token.getSize();
		}
		return false;
	}
}
