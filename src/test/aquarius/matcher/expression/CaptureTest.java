package aquarius.matcher.expression;

import static aquarius.matcher.Expressions.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.runtime.Token;

public class CaptureTest extends TestBase<Token> {
	@Before
	public void prepare() {
		this.expr = $(str("1"), ch().r('a', 'z'), ANY, ch('A', 'Z', 'E'));
		this.initContext("1f4Z");
	}

	@Test
	public void test() {
		// test1
		Token expectedToken = this.input.createToken(0, 4);
		boolean result = this.expr.parse(this.context);
		assertTrue(result);
		assertEquals(expectedToken, this.context.popValue(Token.class));
		assertEquals(4, this.input.getPosition());

		// test2
		this.expr = $();
		this.initContext("12");
		result = this.expr.parse(this.context);
		assertTrue(result);
		assertEquals("", ((Token) this.context.popValue()).getText(this.input));
		assertEquals(0, this.input.getPosition());

		// failure test
		this.expr = $(str("1"), ch().r('a', 'z'), ANY, ch('A', 'Z', 'E'));
		this.initContext("1f4D");
		result = this.expr.parse(this.context);
		assertTrue(!result);
		assertEquals(3, context.popFailure().getFailurePos());
		assertEquals(0, this.input.getPosition());
	}
}
