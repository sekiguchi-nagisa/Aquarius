/*
 * Copyright (C) 2015 Nagisa Sekiguchi
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

public class AnyTest extends TestBase<Void> {

	@Before
	public void prepare() {
		this.expr = ANY;
		this.initContext("@");
	}

	@Test
	public void test() {
		// test1
		boolean result = this.expr.parse(this.context);
		this.success(result, 1);
		assertNull(context.popValue());

		// test2
		this.initContext("ω");
		result = this.expr.parse(this.context);
		this.success(result, 2);
		assertNull(context.popValue());

		// test3
		this.initContext("あ");
		result = this.expr.parse(this.context);
		this.success(result, 3);
		assertNull(context.popValue());

		// test4
		this.initContext(new String(Character.toChars(0x21c56)));
		result = this.expr.parse(this.context);
		this.success(result, 4);
		assertNull(context.popValue());

		// failure test
		this.initContext("");
		result = this.expr.parse(this.context);
		this.failure(result, 0, 0);
	}
}
