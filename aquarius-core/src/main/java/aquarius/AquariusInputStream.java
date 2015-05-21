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
    int EOF = -1;

    /**
     * get current input position.
     *
     * @return current position.
     * pos > -1 && pos <= getInputSize()
     */
    int getPosition();

    /**
     * change input position
     *
     * @param position
     * @throws IndexOutOfBoundsException if position < 0
     *                                   if position > input size
     */
    void setPosition(int position) throws IndexOutOfBoundsException;

    /**
     * get utf8 character in current position.
     *
     * @return if input is end of file, return EOF.
     */
    int fetch();

    /**
     * consume utf8 character in current position and increment position (1-4).
     */
    void consume();

    /**
     * get byte in current position.
     *
     * @return if input is end of file, return EOF.
     */
    int fetchByte();

    /**
     * consume byte in current position and increment position.
     */
    void consumeByte();

    /**
     * @return may be null
     */
    String getSourceName();

    void setSourceName(String sourceName);

    /**
     * create token.
     *
     * @param startPos token start position. inclusive.
     * @param length   token length. may be 0
     * @return
     * @throws IndexOutOfBoundsException if startPos < 0, startPos > getInputSize()
     *                                   if length < 0
     */
    Token createToken(int startPos, int length) throws IndexOutOfBoundsException;

    /**
     * create token. it is equivalent to createToken(startPos, getPosition() - startPos)
     *
     * @param startPos token start position. inclusive.
     * @return
     * @throws IndexOutOfBoundsException if startPos > getPosition()
     *                                   if startPos < 0
     */
    default Token createToken(int startPos) throws IndexOutOfBoundsException {
        return this.createToken(startPos, this.getPosition() - startPos);
    }

    // some token api

    String getTokenText(Token token);

    /**
     * get line number of token.
     *
     * @param token
     * @return
     */
    int getLineNumber(Token token);

    /**
     * get position of starting new line
     *
     * @param token
     * @return
     */
    int getLineStartPos(Token token);

    /**
     * get position in line
     *
     * @param token
     * @return
     */
    default int getPosInLine(Token token) {
        return token.getStartPos() - this.getLineStartPos(token);
    }

    /**
     * get utf8 code position in line
     *
     * @param token
     * @return not equivalent to getPosInLine if has some utf8 characters.
     */
    default int getCodePosInLine(Token token) {
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
