/*
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


package aquarius;

/**
 * represent for token
 * @author skgchxngsxyz-opensuse
 *
 */
public interface Token {
	/**
	 * get token start position. inclusive
	 * @return
	 * 
	 */
	public int getStartPos();

	/**
	 * get token text size.
	 * @return
	 */
	public int getSize();

	/**
	 * get token text.
	 * @param srcInput
	 * token source. not null.
	 * @return
	 */
	public String getText(AquariusInputStream srcInput);

	/**
	 * get line number of token
	 * @param srcInput
	 * token source. not null
	 * @return
	 */
	public int getLineNumber(AquariusInputStream srcInput);

	/**
	 * get position of starting new line
	 * @param srcInput
	 * @return
	 */
	public int getLineStartPos(AquariusInputStream srcInput);

	/**
	 * get position in line.
	 * @param srcInput
	 * not null
	 * @return
	 */
	public default int getPosInLine(AquariusInputStream srcInput) {
		return this.getStartPos() - this.getLineStartPos(srcInput);
	}

	/**
	 * get utf8 code position in line.
	 * @param srcInput
	 * not null
	 * @return
	 * not equivalent to getPosInLine if has some utf8 characters.
	 */
	public default int getCodePosInLine(AquariusInputStream srcInput) {
		int curPos = srcInput.getPosition();

		final int startPos = this.getStartPos();
		int pos = this.getLineStartPos(srcInput);
		srcInput.setPosition(pos);
		int count = 0;
		while(pos < startPos) {
			srcInput.consume();
			pos = srcInput.getPosition();
			count++;
		}

		srcInput.setPosition(curPos);
		return count;
	}
}
