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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import static aquarius.Expressions.*;

public class LiteralTest extends TestBase<Void> {
	@Before
	public void prepare() {
		this.expr = str("hello world");
		this.initContext("hello worldhfieur");
	}
	@Test
	public void test() {
		// test1
		boolean result = this.expr.parse(this.context);
		this.success(result, 11);
		assertNull(context.popValue());

		// test2
		this.expr = str("aこ12");
		this.initContext("aこ12hur");
		result = this.expr.parse(this.context);
		this.success(result, 6);
		assertNull(context.popValue());

		// failure test
		this.initContext("hellod world");
		this.expr = str("hello world");
		result = this.expr.parse(this.context);
		this.failure(result, 0, 5);
	}
}
