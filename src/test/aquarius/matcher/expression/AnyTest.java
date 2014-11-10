package aquarius.matcher.expression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import static aquarius.matcher.Expressions.*;

public class AnyTest extends TestBase<Void> {

	@Before
	public void prepare() {
		this.expr = ANY;
		this.initContext("@");
	}

	@Test
	public void test() {
		// test1
		boolean result = this.expr.parse(this.context);
		assertTrue(result);
		assertEquals(1, this.input.getPosition());

		// test2
		this.initContext("ω");
		result = this.expr.parse(this.context);
		assertTrue(result);
		assertEquals(2, this.input.getPosition());

		// test3
		this.initContext("あ");
		result = this.expr.parse(this.context);
		assertTrue(result);
		assertEquals(3, this.input.getPosition());

		// test4
		this.initContext(new String(Character.toChars(0x21c56)));
		result = this.expr.parse(this.context);
		assertTrue(result);
		assertEquals(4, this.input.getPosition());

		// failure test
		this.initContext("");
		result = this.expr.parse(this.context);
		assertTrue(!result);
		assertEquals(0, this.input.getPosition());
		assertEquals(0, context.popFailure().getFailurePos());
	}
}
