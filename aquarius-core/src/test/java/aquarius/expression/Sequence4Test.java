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
 *//*
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

import static aquarius.Expressions.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.misc.Tuple4;

public class Sequence4Test extends TestBase<Tuple4<Void, Void, Void, Void>> {
	@Before
	public void prepare() {
		this.expr = seq(str("1234"), str("abc"), ANY, ch('a', '2'));
		this.initContext("1234abc32");
	}

	@Test
	public void test() {
		boolean result = this.expr.parse(this.context);
		this.success(result, 9);
		assertNull(context.popValue());

		// failure test
		this.initContext("1234aa");
		result = this.expr.parse(this.context);
		this.failure(result, 0, 5);
	}
}
