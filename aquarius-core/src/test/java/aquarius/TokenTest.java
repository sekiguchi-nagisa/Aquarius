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

import aquarius.misc.Utf8Util;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokenTest {

    @Test
    public void test() {
        // test1
        CommonStream input = new CommonStream("sample", "a");
        assertEquals("sample", input.getSourceName());
        assertEquals(1, input.getBufferSize());
        assertEquals(Utf8Util.toUtf8Code('a'), input.fetch());
        input.consume();
        assertEquals(1, input.getPosition());
        assertEquals(AquariusInputStream.EOF, input.fetch());
        Token token = input.createToken(0);
        assertEquals("a", input.getTokenText(token));
        assertEquals(1, token.getSize());

        // test2
        input = new CommonStream("sample", "ω");
        assertEquals("sample", input.getSourceName());
        assertEquals(2, input.getBufferSize());
        assertEquals(Utf8Util.toUtf8Code('ω'), input.fetch());
        input.consume();
        assertEquals(2, input.getPosition());
        assertEquals(AquariusInputStream.EOF, input.fetch());
        token = input.createToken(0);
        assertEquals("ω", input.getTokenText(token));
        assertEquals(2, token.getSize());

        // test3
        input = new CommonStream("sample", "あ");
        assertEquals("sample", input.getSourceName());
        assertEquals(3, input.getBufferSize());
        assertEquals(Utf8Util.toUtf8Code('あ'), input.fetch());
        input.consume();
        assertEquals(3, input.getPosition());
        assertEquals(AquariusInputStream.EOF, input.fetch());
        token = input.createToken(0);
        assertEquals("あ", input.getTokenText(token));
        assertEquals(3, token.getSize());

        String s = new String(Character.toChars(0x21c56));
        // test4
        input = new CommonStream("sample", s);
        assertEquals("sample", input.getSourceName());
        assertEquals(4, input.getBufferSize());
        assertEquals(Utf8Util.toUtf8Code(0x21c56), input.fetch());
        input.consume();
        assertEquals(4, input.getPosition());
        assertEquals(AquariusInputStream.EOF, input.fetch());
        token = input.createToken(0);
        assertEquals(s, input.getTokenText(token));
        assertEquals(4, token.getSize());
    }

}
