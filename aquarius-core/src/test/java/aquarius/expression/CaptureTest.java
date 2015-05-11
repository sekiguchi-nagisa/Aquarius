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

import aquarius.Token;
import org.junit.Before;
import org.junit.Test;

import static aquarius.Expressions.*;
import static org.junit.Assert.assertEquals;

public class CaptureTest extends TestBase<Token> {
    @Before
    public void prepare() {
        this.expr = $(str("1"), r('a', 'z'), ANY, ch('A', 'Z', 'E'));
        this.initContext("1f4Z");
    }

    @Test
    public void test() {
        // test1
        Token expectedToken = this.input.createToken(0, 4);
        boolean result = this.expr.parse(this.context);
        this.success(result, 4);
        assertEquals(expectedToken, this.context.popValue(Token.class));

        // test2
        this.expr = $("12");
        this.initContext("12");
        result = this.expr.parse(this.context);
        this.success(result, 2);
        assertEquals("12", this.input.getTokenText((Token) this.context.popValue()));

        // failure test
        this.expr = $(str("1"), r('a', 'z'), ANY, ch('A', 'Z', 'E'));
        this.initContext("1f4D");
        result = this.expr.parse(this.context);
        this.failure(result, 0, 3);
    }
}
