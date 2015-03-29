package aquarius;

import static org.junit.Assert.*;

import org.junit.Test;

public class LineNumberTest {
	@Test
	public void test() {
		// test1
		AquariusInputStream input = new CommonStream("sample", "hogehoge");
		assertEquals(1, input.getLineNumber(input.createToken(2, 2)));

		// test2
		input = new CommonStream("sample", "\nhoge\nhuga\n");
		assertEquals(1, input.getLineNumber(input.createToken(0, 2)));
		assertEquals(2, input.getLineNumber(input.createToken(2, 2)));
		assertEquals(3, input.getLineNumber(input.createToken(6, 1)));
		assertEquals(3, input.getLineNumber(input.createToken(10, 0)));
	}

}
