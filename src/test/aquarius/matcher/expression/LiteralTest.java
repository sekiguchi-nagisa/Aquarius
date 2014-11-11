package aquarius.matcher.expression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import static aquarius.matcher.Expressions.*;

public class LiteralTest extends TestBase<Void> {
	@Before
	public void prepare() {
		this.expr = str("hello world");
		this.initContext("hello worldhfieur");
	}
	@Test
	public void test() {
		// test1
		boolean result = this.expr.parse(this.context);
		assertTrue(result);
		assertNull(context.popValue());
		assertEquals(11, this.input.getPosition());

		// test2
		this.expr = str("aこ12");
		this.initContext("aこ12hur");
		result = this.expr.parse(this.context);
		assertTrue(result);
		assertNull(context.popValue());
		assertEquals(6, this.input.getPosition());

		// failure test
		this.initContext("hellod world");
		this.expr = str("hello world");
		result = this.expr.parse(this.context);
		assertTrue(!result);
		assertEquals(0, context.popFailure().getFailurePos());
		assertEquals(0, this.input.getPosition());
	}
}
