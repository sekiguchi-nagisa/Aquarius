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

import org.junit.Before;
import org.junit.Test;

import static aquarius.Expressions.ch;
import static aquarius.misc.Utf8Util.toUtf8Code;
import static org.junit.Assert.assertNull;

public class CharSetTest extends TestBase<Void> {

    @Before
    public void prepare() {
        this.expr = ch('@', '$', toUtf8Code('ω'),
                toUtf8Code('あ'), toUtf8Code(0x21c56)).r('a', 'd').r('3', '5');
        this.initContext("@");
    }

    @Test
    public void test() {
        // test 1
        boolean result = this.expr.parse(this.context);
        this.success(result, 1);
        assertNull(context.popValue());

        // test 2
        this.initContext("a");
        result = this.expr.parse(this.context);
        this.success(result, 1);
        assertNull(context.popValue());

        // test 3
        this.initContext("d");
        result = this.expr.parse(this.context);
        this.success(result, 1);
        assertNull(context.popValue());

        // test 4
        this.initContext("4");
        result = this.expr.parse(this.context);
        this.success(result, 1);
        assertNull(context.popValue());

        // test 5
        this.initContext("ω");
        result = this.expr.parse(this.context);
        this.success(result, 2);
        assertNull(context.popValue());

        // test 6
        this.initContext("あ");
        result = this.expr.parse(this.context);
        this.success(result, 3);
        assertNull(context.popValue());

        // test 7
        String s = new String(Character.toChars(0x21c56));
        this.initContext(s);
        result = this.expr.parse(this.context);
        this.success(result, 4);
        assertNull(context.popValue());

        // failure test
        this.initContext("E");
        result = this.expr.parse(this.context);
        this.failure(result, 0, 0);

        this.initContext("");
        result = this.expr.parse(this.context);
        this.failure(result, 0, 0);
    }
}
