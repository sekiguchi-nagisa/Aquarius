package aquarius.matcher.expression;

import static aquarius.matcher.Expressions.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ChoiceTest extends TestBase<Void> {
	@Before
	public void prepare() {
		this.expr = choice(str("hello"), str("world"), str("good"));
		this.initContext("hello");
	}

	@Test
	public void test() {
		// test1
		boolean result = this.expr.parse(this.context);
		assertTrue(result);
		assertNull(context.popValue());
		assertEquals(5, this.input.getPosition());

		// test2
		this.initContext("good");
		result = this.expr.parse(this.context);
		assertTrue(result);
		assertNull(context.popValue());
		assertEquals(4, this.input.getPosition());

		// test3
		this.initContext("world");
		result = this.expr.parse(this.context);
		assertTrue(result);
		assertNull(context.popValue());
		assertEquals(5, this.input.getPosition());

		// failure test
		this.initContext("w");
		boolean result2 = this.expr.parse(this.context);
		assertTrue(!result2);
		assertEquals(0, context.popFailure().getFailurePos());
		assertEquals(0, this.input.getPosition());
	}
}
