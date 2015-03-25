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

package aquarius.misc;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.Arrays;

import org.junit.Test;

import aquarius.misc.TypeMatch.When;

public class TypeMatchTest {

	@Test
	public void test() {
		When<?> when1 = new When<>(Exception.class, null);
		When<?> when2 = new When<>(IllegalArgumentException.class, null);
		When<?> when3 = new When<>(Object.class, null);
		When<?> when4 = new When<>(Throwable.class, null);

		When<?>[] whens = new When[] {when1, when2, when3, when4};
		Arrays.sort(whens);

		// test1
		assertEquals(IllegalArgumentException.class, whens[0].targetClass);
		assertEquals(Exception.class, whens[1].targetClass);
		assertEquals(Throwable.class, whens[2].targetClass);
		assertEquals(Object.class, whens[3].targetClass);

		// test2
		IllegalArgumentException e = new IllegalArgumentException();
		assertEquals(true, when1.match(e));
		assertEquals(true, when2.match(e));
		assertEquals(false, when2.match(new FileNotFoundException()));
		assertEquals(true, when3.match(e));
		assertEquals(true, when4.match(e));
	}
}
