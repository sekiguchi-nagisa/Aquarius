package aquarius.matcher.expression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.runtime.Result;
import aquarius.runtime.Result.Failure;
import aquarius.runtime.Token;
import static aquarius.matcher.expression.ParsingExpression.*;

public class LiteralTest extends TestBase<Token> {
	private final String text = "hello world";
	@Before
	public void prepare() {
		this.expr = str(text);
		this.initContext(text + "hfieur");
	}
	@Test
	public void test() {
		Token expected = this.input.createToken(0, text.length());
		Result<Token> result = this.expr.parse(this.context);
		assertEquals(expected, result.get());
		assertEquals(11, this.input.getPosition());

		// failure test
		this.initContext("hellod world");
		result = this.expr.parse(this.context);
		assertTrue(result.isFailure());
		assertEquals(5, ((Failure<?>) result).getFailurePos());
		assertEquals(0, this.input.getPosition());
	}
}
