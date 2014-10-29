package aquarius.matcher.expression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.runtime.Result;
import aquarius.runtime.Result.Failure;
import aquarius.runtime.Token;
import static aquarius.matcher.expression.ParsingExpression.*;
import static aquarius.util.Utf8Util.*;

public class CharSetTest extends TestBase<Token> {

	@Before
	public void prepare() {
		this.expr = ch('@', '$', toUtf8Code('ω'), 
				toUtf8Code('あ'),toUtf8Code(0x21c56))._r('a', 'd')._r('3', '5');
		this.initContext("@");
	}

	@Test
	public void test() {
		// test 1
		Token expected = this.input.createToken(0, 1);
		Result<Token> result = this.expr.parse(this.context);
		assertEquals(expected, result.get());
		assertEquals(1, this.input.getPosition());

		// test 2
		this.initContext("a");
		expected = this.input.createToken(0, 1);
		result = this.expr.parse(this.context);
		assertEquals(expected, result.get());
		assertEquals(1, this.input.getPosition());

		// test 3
		this.initContext("d");
		expected = this.input.createToken(0, 1);
		result = this.expr.parse(this.context);
		assertEquals(expected, result.get());
		assertEquals(1, this.input.getPosition());

		// test 4
		this.initContext("4");
		expected = this.input.createToken(0, 1);
		result = this.expr.parse(this.context);
		assertEquals(expected, result.get());
		assertEquals(1, this.input.getPosition());

		// test 5
		this.initContext("ω");
		expected = this.input.createToken(0, 2);
		result = this.expr.parse(this.context);
		assertEquals(expected, result.get());
		assertEquals(2, this.input.getPosition());

		// test 6
		this.initContext("あ");
		expected = this.input.createToken(0, 3);
		result = this.expr.parse(this.context);
		assertEquals(expected, result.get());
		assertEquals(3, this.input.getPosition());

		// test 7
		String s = new String(Character.toChars(0x21c56));
		this.initContext(s);
		expected = this.input.createToken(0, 4);
		result = this.expr.parse(this.context);
		assertEquals(expected, result.get());
		assertEquals(4, this.input.getPosition());

		// failure test
		this.initContext("E");
		result = this.expr.parse(this.context);
		assertTrue(result.isFailure());
		assertEquals(0, ((Failure<?>) result).getFailurePos());
		assertEquals(0, this.input.getPosition());

		this.initContext("");
		result = this.expr.parse(this.context);
		assertTrue(result.isFailure());
		assertEquals(0, ((Failure<?>) result).getFailurePos());
		assertEquals(0, this.input.getPosition());
	}
}
