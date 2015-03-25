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

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class OneMoreTest extends TestBase<List<Void>> {
	@Before
	public void prepare() {
		this.expr = str("hello").oneMore();
		this.initContext("hellohello");
	}

	@Test
	public void test() {
		// test1
		boolean result = this.expr.parse(this.context);
		this.success(result, 10);
		assertNull(context.popValue());

		// test2
		this.initContext("hellos");
		result = this.expr.parse(this.context);
		this.success(result, 5);
		assertNull(context.popValue());

		// failure test
		this.initContext("hells");
		result = this.expr.parse(this.context);
		this.failure(result, 0, 4);
	}
}
