package aquarius.util;

import static aquarius.util.Utf8Util.*;

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
		int[] result = toUtfCodes(a);
		assertEquals(1, result.length);
		assertEquals(expected, result[0]);

		// test3
		assertEquals(a, codeToString(expected));
		assertEquals("ああ", codesToString(new int[]{expected, expected}));
	}

}
