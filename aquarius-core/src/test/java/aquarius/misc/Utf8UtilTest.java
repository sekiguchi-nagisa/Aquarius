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


package aquarius.misc;

import static aquarius.misc.Utf8Util.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8UtilTest {

	@Test
	public void test() {
		// test1
		int expected = 0xe38182;
		byte[] buf = new byte[]{(byte) 0xe3, (byte) 0x81, (byte) 0x82};
		assertEquals(expected, toUtf8Code(buf, 0, buf.length));

		// test2
		String a = "あ";
		int[] result = toUtf8Codes(a);
		assertEquals(1, result.length);
		assertEquals(expected, result[0]);

		// test3
		assertEquals(a, codeToString(expected));
		assertEquals("ああ", codesToString(new int[]{expected, expected}));
	}

}
