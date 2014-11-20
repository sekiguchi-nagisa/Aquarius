package aquarius;

import static org.junit.Assert.*;

import org.junit.Test;

import aquarius.AquariusInputStream;
import aquarius.CommonStream;
import aquarius.Token;
import aquarius.misc.Utf8Util;

public class TokenTest {

	@Test
	public void test() {
		// test1
		AquariusInputStream input = new CommonStream("sample", "a");
		assertEquals("sample", input.getSourceName());
		assertEquals(1, input.getInputSize());
		assertEquals(Utf8Util.toUtf8Code('a'), input.fetch());
		input.consume();
		assertEquals(1, input.getPosition());
		assertEquals(AquariusInputStream.EOF, input.fetch());
		Token token = input.createToken(0);
		assertEquals("a", token.getText(input));
		assertEquals(1, token.getSize());

		// test2
		input = new CommonStream("sample", "ω");
		assertEquals("sample", input.getSourceName());
		assertEquals(2, input.getInputSize());
		assertEquals(Utf8Util.toUtf8Code('ω'), input.fetch());
		input.consume();
		assertEquals(2, input.getPosition());
		assertEquals(AquariusInputStream.EOF, input.fetch());
		token = input.createToken(0);
		assertEquals("ω", token.getText(input));
		assertEquals(2, token.getSize());

		// test3
		input = new CommonStream("sample", "あ");
		assertEquals("sample", input.getSourceName());
		assertEquals(3, input.getInputSize());
		assertEquals(Utf8Util.toUtf8Code('あ'), input.fetch());
		input.consume();
		assertEquals(3, input.getPosition());
		assertEquals(AquariusInputStream.EOF, input.fetch());
		token = input.createToken(0);
		assertEquals("あ", token.getText(input));
		assertEquals(3, token.getSize());

		String s = new String(Character.toChars(0x21c56));
		// test4
		input = new CommonStream("sample", s);
		assertEquals("sample", input.getSourceName());
		assertEquals(4, input.getInputSize());
		assertEquals(Utf8Util.toUtf8Code(0x21c56), input.fetch());
		input.consume();
		assertEquals(4, input.getPosition());
		assertEquals(AquariusInputStream.EOF, input.fetch());
		token = input.createToken(0);
		assertEquals(s, token.getText(input));
		assertEquals(4, token.getSize());
	}

}
