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

import aquarius.misc.Tuple2;
import static aquarius.Expressions.*;

public class NotTest extends TestBase<Tuple2<Void, Void>> {
	@Before
	public void prepare() {
		this.expr = seq(str("public"), not(r('a', 'z').r('A', 'Z').r('0', '9').oneMore()));
		this.initContext("public   \t   \t    \t\t");
	}
	@Test
	public void test() {
		boolean result = this.expr.parse(this.context);
		this.success(result, 6);
		assertNull(context.popValue());

		// failure test
		this.initContext("publicd  ");
		result = this.expr.parse(this.context);
		this.failure(result, 0, 6);
	}
}
