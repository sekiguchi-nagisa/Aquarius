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

import aquarius.CommonStream;
import aquarius.ParserContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Base class for EvalTest
 *
 * @param <R>
 * @param <R>
 * @author skgchxngsxyz-opensuse
 */
public abstract class TestBase<R> {
    protected CommonStream input;

    protected ParsingExpression<R> expr;

    protected ParserContext context;

    protected void initContext(String source) {
        this.input = new CommonStream("test", source);
        this.context = new ParserContext(this.input);
    }

    protected void success(boolean status, int position) {
        assertTrue(status);
        assertEquals(position, this.input.getPosition());
    }

    protected void failure(boolean status, int position, int failruePos) {
        assertTrue(!status);
        assertEquals(position, this.input.getPosition());
        assertEquals(failruePos, this.context.getFailure().getFailurePos());
    }
}
