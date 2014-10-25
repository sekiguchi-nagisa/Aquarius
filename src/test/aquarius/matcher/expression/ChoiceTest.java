package aquarius.matcher.expression;

import static aquarius.matcher.expression.ParsingExpression.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.runtime.Result;
import aquarius.runtime.Token;
import aquarius.runtime.Result.Failure;

public class ChoiceTest extends TestBase<Token> {
	@Before
	public void prepare() {
		this.expr = choice(str("hello"), str("world"), str("good"));
		this.initContext("hello");
	}

	@Test
	public void test() {
		// test1
		String result = this.expr.parse(this.context).get().getText(this.input);
		assertEquals("hello", result);
		assertEquals(5, this.input.getPosition());

		// test2
		this.initContext("good");
		result = this.expr.parse(this.context).get().getText(this.input);
		assertEquals("good", result);
		assertEquals(4, this.input.getPosition());

		// test3
		this.initContext("world");
		result = this.expr.parse(this.context).get().getText(this.input);
		assertEquals("world", result);
		assertEquals(5, this.input.getPosition());

		// failure test
		this.initContext("w");
		Result<Token> result2 = this.expr.parse(this.context);
		assertTrue(result2.isFailure());
		assertEquals(1, ((Failure<?>) result2).getFailurePos());
		assertEquals(0, this.input.getPosition());
	}
}
