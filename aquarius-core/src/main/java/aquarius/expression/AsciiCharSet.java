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

package aquarius.expression;

import aquarius.AquariusInputStream;
import aquarius.ParserContext;
import aquarius.misc.IntRange;
import aquarius.misc.Utf8Util;

import java.util.Arrays;

/**
 * try to match one ascii character from char set.
 *
 * @author skgchxngsxyz-opensuse
 */
public class AsciiCharSet extends CharSet {
    private final boolean[] asciiMap;

    public AsciiCharSet(int[] chars) {
        super(chars);
        this.asciiMap = new boolean[128];
        Arrays.fill(this.asciiMap, false);
        this.updateAsciiMap(chars);
    }

    @Override
    public CharSet r(int start, int stop) throws IllegalArgumentException {
        super.r(start, stop);
        if(Utf8Util.isAsciiCode(start) && Utf8Util.isAsciiCode(stop)) {
            this.updateAsciiMap(start, stop);
            return this;
        }
        CharSet newCharSet = new CharSet(this.getChars());
        for(IntRange range : this.rangeList) {
            newCharSet.r(range.getStart(), range.getStop());
        }
        return newCharSet;
    }

    /**
     * set characters to ascii map
     *
     * @param chars must be ascii characters.
     */
    private void updateAsciiMap(int[] chars) {
        for(int ch : chars) {
            this.updateAsciiMap(ch);
        }
    }

    /**
     * set characters(start ~ stop) to ascii map
     *
     * @param start must be ascii character.
     * @param stop  must be ascii character.
     */
    private void updateAsciiMap(int start, int stop) {
        int size = stop - start + 1;
        for(int i = 0; i < size; i++) {
            this.updateAsciiMap(start + i);
        }
    }

    /**
     * set character to ascii map
     *
     * @param ch must be ascii character. (ch > -1 && ch < 128)
     */
    private void updateAsciiMap(int ch) {
        this.asciiMap[ch] = true;
    }

    @Override
    public boolean parse(ParserContext context) {
        AquariusInputStream input = context.getInputStream();
        final int fetchedCh = input.fetchByte();

        if(Utf8Util.isAsciiCode(fetchedCh)) {
            if(this.asciiMap[fetchedCh]) {
                input.consumeByte();
                return true;
            }
        }
        context.pushFailure(input.getPosition(), this);
        return false;
    }
}
