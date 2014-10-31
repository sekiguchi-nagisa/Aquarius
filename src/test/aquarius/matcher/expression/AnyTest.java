package aquarius.matcher.expression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.runtime.Result;
import aquarius.runtime.Result.Failure;
import aquarius.runtime.Token;
import static aquarius.matcher.Expressions.*;

public class AnyTest extends TestBase<Token> {

	@Before
	public void prepare() {
		this.expr = ANY;
		this.initContext("@");
	}

	@Test
	public void test() {
		// test1
		Token expected = this.input.createToken(0, 1);
		Result<Token> result = this.expr.parse(this.context);
		assertEquals(expected, result.get());
		assertEquals(1, this.input.getPosition());

		// test2
		this.initContext("ω");
		expected = this.input.createToken(0, 2);
		result = this.expr.parse(this.context);
		assertEquals(expected, result.get());
		assertEquals(2, this.input.getPosition());

		// test3
		this.initContext("あ");
		expected = this.input.createToken(0, 3);
		result = this.expr.parse(this.context);
		assertEquals(expected, result.get());
		assertEquals(3, this.input.getPosition());

		// test4
		this.initContext(new String(Character.toChars(0x21c56)));
		expected = this.input.createToken(0, 4);
		result = this.expr.parse(this.context);
		assertEquals(expected, result.get());
		assertEquals(4, this.input.getPosition());

		// failure test
		this.initContext("");
		result = this.expr.parse(this.context);
		assertTrue(result.isFailure());
		assertEquals(0, this.input.getPosition());
		assertEquals(((Failure<?>) result).getFailurePos(), 0);
	}
}
