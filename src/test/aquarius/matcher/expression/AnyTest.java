package aquarius.matcher.expression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.runtime.Result;
import aquarius.runtime.Result.Failure;
import aquarius.runtime.Token;
import static aquarius.matcher.expression.ParsingExpression.*;

public class AnyTest extends TestBase<Token> {

	@Before
	public void prepare() {
		this.expr = any();
		this.initContext("G");
	}

	@Test
	public void test() {
		Token expected = this.input.createToken(0, 1);
		Result<Token> result = this.expr.parse(this.context);
		assertEquals(expected, result.get());
		assertEquals(1, this.input.getPosition());

		// failure test
		this.initContext("");
		result = this.expr.parse(this.context);
		assertTrue(result.isFailure());
		assertEquals(0, this.input.getPosition());
		assertEquals(((Failure<?>) result).getFailurePos(), 0);
	}
}
