package aquarius.matcher.expression;

import static aquarius.matcher.Expressions.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.runtime.Token;

public class CaptureTest extends TestBase<Token> {
	@Before
	public void prepare() {
		this.expr = $(str("1"), r('a', 'z'), ANY, ch('A', 'Z', 'E'));
		this.initContext("1f4Z");
	}

	@Test
	public void test() {
		// test1
		Token expectedToken = this.input.createToken(0, 4);
		boolean result = this.expr.parse(this.context);
		this.success(result, 4);
		assertEquals(expectedToken, this.context.popValue(Token.class));

		// test2
		this.expr = $();
		this.initContext("12");
		result = this.expr.parse(this.context);
		this.success(result, 0);
		assertEquals("", ((Token) this.context.popValue()).getText(this.input));

		// failure test
		this.expr = $(str("1"), r('a', 'z'), ANY, ch('A', 'Z', 'E'));
		this.initContext("1f4D");
		result = this.expr.parse(this.context);
		this.failure(result, 0, 3);
	}
}
