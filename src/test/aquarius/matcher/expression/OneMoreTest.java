package aquarius.matcher.expression;

import static aquarius.matcher.expression.ParsingExpression.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;

import aquarius.runtime.Result;
import aquarius.runtime.Token;
import aquarius.runtime.Result.Failure;

import org.junit.Test;

public class OneMoreTest extends TestBase<List<Token>>{
	@Before
	public void prepare() {
		this.expr = oneMore(str("hello"));
		this.initContext("hellohello");
	}

	@Test
	public void test() {
		// test1
		String expectedText = "hello";
		Result<List<Token>> result = this.expr.parse(this.context);
		assertEquals(2, result.get().size());
		assertEquals(expectedText, result.get().get(0).getText(this.input));
		assertEquals(expectedText, result.get().get(1).getText(this.input));
		assertEquals(10, this.input.getPosition());

		// test2
		this.initContext("hellos");
		result = this.expr.parse(this.context);
		assertEquals(1, result.get().size());
		assertEquals(expectedText, result.get().get(0).getText(this.input));
		assertEquals(5, this.input.getPosition());

		// failure test
		this.initContext("hells");
		result = this.expr.parse(this.context);
		assertTrue(result.isFailure());
		assertEquals(4, ((Failure<?>) result).getFailurePos());
		assertEquals(0, this.input.getPosition());
	}
}
