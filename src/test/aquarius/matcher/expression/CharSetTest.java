package aquarius.matcher.expression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.runtime.Result;
import aquarius.runtime.Result.Failure;
import aquarius.runtime.Token;
import static aquarius.matcher.expression.ParsingExpression.*;

public class CharSetTest extends TestBase<Token> {

	@Before
	public void prepare() {
		this.expr = ch('@', '$')._r('a', 'd')._r('3', '5');
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