package aquarius.matcher.expression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.runtime.Result;
import aquarius.runtime.Result.Failure;
import aquarius.runtime.Token;
import static aquarius.matcher.expression.ParsingExpression.*;

public class LiteralTest extends TestBase<Token> {
	@Before
	public void prepare() {
		this.expr = str("hello world");
		this.initContext("hello worldhfieur");
	}
	@Test
	public void test() {
		// test1
		Token expected = this.input.createToken(0, "hello world".length());
		Result<Token> result = this.expr.parse(this.context);
		assertEquals(expected, result.get());
		assertEquals(11, this.input.getPosition());

		// test2
		String text = "aこ12";
		this.expr = str("aこ12");
		this.initContext("aこ12hur");
		result = this.expr.parse(this.context);
		assertEquals(text, result.get().getText(this.input));
		assertEquals(6, this.input.getPosition());

		// failure test
		this.initContext("hellod world");
		this.expr = str("hello world");
		result = this.expr.parse(this.context);
		assertTrue(result.isFailure());
		assertEquals(5, ((Failure<?>) result).getFailurePos());
		assertEquals(0, this.input.getPosition());
	}
}
