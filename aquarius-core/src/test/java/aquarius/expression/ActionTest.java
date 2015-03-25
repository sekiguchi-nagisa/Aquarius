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

import aquarius.action.FailedActionException;
import static aquarius.Expressions.*;

public class ActionTest extends TestBase<Integer> {
	@Before
	public void prepare() {
		this.expr = ANY.map((ctx, a) -> 12);
		this.initContext("hfreui");
	}
	@Test
	public void test() {
		boolean result = this.expr.parse(this.context);
		this.success(result, 1);
		assertEquals(12, this.context.popValue(Integer.class).intValue());

		// failure test
		this.expr = ANY.map((ctx, a) -> {throw new FailedActionException("fail");});
		this.initContext("12+34");
		result = this.expr.parse(this.context);
		this.failure(result, 0, 1);
	}
}
