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

package aquarius.example;

import aquarius.CommonStream;
import aquarius.ParsedResult;
import aquarius.ParserFactory;
import aquarius.Token;
import org.junit.Test;

import static org.junit.Assert.*;

public class SampleParserTest {
    @Test
    public void test() {
        SampleParser parser = ParserFactory.createParser(SampleParser.class);

        String source = "      (1+ 3) / 2      ";
        CommonStream input = new CommonStream("ex", source);

        ParsedResult<Token> result = parser.Expr().parse(input);
        assertTrue(result.isSuccess());
        assertEquals(input.getPosition(), input.getBufferSize());
        assertEquals(input.getTokenText(result.getValue()), source.trim());

        // parser instance test
        assertNotNull(parser.toString());
        assertEquals(parser.hashCode(), parser.hashCode());
    }
}
