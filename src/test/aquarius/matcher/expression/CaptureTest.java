package aquarius.matcher.expression;

import static aquarius.matcher.Expressions.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.runtime.Result;
import aquarius.runtime.Token;
import aquarius.runtime.Result.Failure;

public class CaptureTest extends TestBase<Token> {
	@Before
	public void prepare() {
		this.expr = $(str("1"), ch()._r('a', 'z'), any(), ch('A', 'Z', 'E'));
		this.initContext("1f4Z");
	}

	@Test
	public void test() {
		Token expectedToken = this.input.createToken(0, 4);
		Result<Token> result = this.expr.parse(this.context);
		assertEquals(expectedToken, result.get());
		assertEquals(4, this.input.getPosition());

		// failure test
		this.initContext("1f4D");
		result = this.expr.parse(this.context);
		assertTrue(result.isFailure());
		assertEquals(3, ((Failure<?>)result).getFailurePos());
		assertEquals(0, this.input.getPosition());
	}
}
