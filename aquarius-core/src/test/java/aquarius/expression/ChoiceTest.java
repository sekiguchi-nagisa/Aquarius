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

import static aquarius.Expressions.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ChoiceTest extends TestBase<Void> {
	@Before
	public void prepare() {
		this.expr = str("hello").or(str("world")).or(str("good"));
		this.initContext("hello");
	}

	@Test
	public void test() {
		// test1
		boolean result = this.expr.parse(this.context);
		this.success(result, 5);
		assertNull(context.popValue());

		// test2
		this.initContext("good");
		result = this.expr.parse(this.context);
		this.success(result, 4);
		assertNull(context.popValue());

		// test3
		this.initContext("world");
		result = this.expr.parse(this.context);
		this.success(result, 5);
		assertNull(context.popValue());

		// failure test
		this.initContext("w");
		boolean result2 = this.expr.parse(this.context);
		this.failure(result2, 0, 1);
	}
}
