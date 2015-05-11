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

public class FilterTest extends TestBase<Token> {
    private String getText() {
        return this.input.getTokenText(this.context.popValue(Token.class));
    }

    @Before
    public void prepare() {
        this.expr = seq($("hello"), ANY).filter0();
        this.initContext("hello46");
    }

    @Test
    public void test() {
        // test1
        boolean result = this.expr.parse(this.context);
        this.success(result, 6);
        assertEquals("hello", this.getText());

        this.expr = seq($('h'), $('0')).filter1();
        this.initContext("h02");
        result = this.expr.parse(this.context);
        this.success(result, 2);
        assertEquals("0", this.getText());

        // test2
        this.expr = seq($('a'), $('b'), $('c')).filter0();
        this.initContext("abc");
        result = this.expr.parse(this.context);
        this.success(result, 3);
        assertEquals("a", this.getText());

        // test2
        this.expr = seq($('a'), $('b'), $('c')).filter1();
        this.initContext("abc");
        result = this.expr.parse(this.context);
        this.success(result, 3);
        assertEquals("b", this.getText());

        // test2
        this.expr = seq($('a'), $('b'), $('c')).filter2();
        this.initContext("abc");
        result = this.expr.parse(this.context);
        this.success(result, 3);
        assertEquals("c", this.getText());

        // test3
        this.expr = seq($('a'), $('b'), $('c'), $('d')).filter0();
        this.initContext("abcd");
        result = this.expr.parse(this.context);
        this.success(result, 4);
        assertEquals("a", this.getText());

        this.expr = seq($('a'), $('b'), $('c'), $('d')).filter1();
        this.initContext("abcd");
        result = this.expr.parse(this.context);
        this.success(result, 4);
        assertEquals("b", this.getText());

        this.expr = seq($('a'), $('b'), $('c'), $('d')).filter2();
        this.initContext("abcd");
        result = this.expr.parse(this.context);
        this.success(result, 4);
        assertEquals("c", this.getText());

        this.expr = seq($('a'), $('b'), $('c'), $('d')).filter3();
        this.initContext("abcd");
        result = this.expr.parse(this.context);
        this.success(result, 4);
        assertEquals("d", this.getText());

        // test4
        this.expr = seq($('a'), $('b'), $('c'), $('d'), $('e')).filter0();
        this.initContext("abcde");
        result = this.expr.parse(this.context);
        this.success(result, 5);
        assertEquals("a", this.getText());

        this.expr = seq($('a'), $('b'), $('c'), $('d'), $('e')).filter1();
        this.initContext("abcde");
        result = this.expr.parse(this.context);
        this.success(result, 5);
        assertEquals("b", this.getText());

        this.expr = seq($('a'), $('b'), $('c'), $('d'), $('e')).filter2();
        this.initContext("abcde");
        result = this.expr.parse(this.context);
        this.success(result, 5);
        assertEquals("c", this.getText());

        this.expr = seq($('a'), $('b'), $('c'), $('d'), $('e')).filter3();
        this.initContext("abcde");
        result = this.expr.parse(this.context);
        this.success(result, 5);
        assertEquals("d", this.getText());

        this.expr = seq($('a'), $('b'), $('c'), $('d'), $('e')).filter4();
        this.initContext("abcde");
        result = this.expr.parse(this.context);
        this.success(result, 5);
        assertEquals("e", this.getText());
    }
}
