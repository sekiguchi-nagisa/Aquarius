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

import java.util.Optional;

import static aquarius.Expressions.str;
import static org.junit.Assert.assertNull;

public class OptionalTest extends TestBase<Optional<Void>> {

    @Before
    public void prepare() {
        this.expr = str("hello").opt();
        this.initContext("g");
    }

    @Test
    public void test() {
        // test 1
        boolean result = this.expr.parse(this.context);
        this.success(result, 0);
        assertNull(context.popValue());

        // test 2
        this.initContext("hello");
        result = this.expr.parse(this.context);
        this.success(result, 5);
    }
}
