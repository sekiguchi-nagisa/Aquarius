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
 * @author skgchxngsxyz-opensuse
 */
public interface AquariusInputStream {
    /**
     * represent for end of file
     */
    public final static int EOF = -1;

    /**
     * get current input position.
     *
     * @return current position.
     * pos > -1 && pos <= getInputSize()
     */
    public int getPosition();

    /**
     * change input position
     *
     * @param position
     * @throws IndexOutOfBoundsException if position < 0
     *                                   if position > input size
     */
    public void setPosition(int position) throws IndexOutOfBoundsException;

    /**
     * get utf8 character in current position.
     *
     * @return if input is end of file, return EOF.
     */
    public int fetch();

    /**
     * consume utf8 character in current position and increment position (1-4).
     */
    public void consume();

    /**
     * get byte in current position.
     *
     * @return if input is end of file, return EOF.
     */
    public int fetchByte();

    /**
     * consume byte in current position and increment position.
     */
    public void consumeByte();

    /**
     * @return may be null
     */
    public String getSourceName();

    public void setSourceName(String sourceName);

    /**
     * create token.
     *
     * @param startPos token start position. inclusive.
     * @param length   token length. may be 0
     * @return
     * @throws IndexOutOfBoundsException if startPos < 0, startPos > getInputSize()
     *                                   if length < 0
     */
    public Token createToken(int startPos, int length) throws IndexOutOfBoundsException;

    /**
     * create token. it is equivalent to createToken(startPos, getPosition() - startPos)
     *
     * @param startPos token start position. inclusive.
     * @return
     * @throws IndexOutOfBoundsException if startPos > getPosition()
     *                                   if startPos < 0
     */
    public default Token createToken(int startPos) throws IndexOutOfBoundsException {
        return this.createToken(startPos, this.getPosition() - startPos);
    }

    // some token api

    public String getTokenText(Token token);

    /**
     * get line number of token.
     *
     * @param token
     * @return
     */
    public int getLineNumber(Token token);

    /**
     * get position of starting new line
     *
     * @param token
     * @return
     */
    public int getLineStartPos(Token token);

    /**
     * get position in line
     *
     * @param token
     * @return
     */
    public default int getPosInLine(Token token) {
        return token.getStartPos() - this.getLineStartPos(token);
    }

    /**
     * get utf8 code position in line
     *
     * @param token
     * @return not equivalent to getPosInLine if has some utf8 characters.
     */
    public default int getCodePosInLine(Token token) {
        int curPos = this.getPosition();

        final int startPos = token.getStartPos();
        int pos = this.getLineStartPos(token);
        this.setPosition(pos);
        int count = 0;
        while(pos < startPos) {
            this.consume();
            pos = this.getPosition();
            count++;
        }

        this.setPosition(curPos);
        return count;
    }
}
