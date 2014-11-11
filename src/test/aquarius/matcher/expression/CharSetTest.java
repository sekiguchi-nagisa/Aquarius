package aquarius.matcher.expression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import static aquarius.matcher.Expressions.*;
import static aquarius.misc.Utf8Util.*;

public class CharSetTest extends TestBase<Void> {

	@Before
	public void prepare() {
		this.expr = ch('@', '$', toUtf8Code('ω'), 
				toUtf8Code('あ'),toUtf8Code(0x21c56)).r('a', 'd').r('3', '5');
		this.initContext("@");
	}

	@Test
	public void test() {
		// test 1
		boolean result = this.expr.parse(this.context);
		assertTrue(result);
		assertNull(context.popValue());
		assertEquals(1, this.input.getPosition());

		// test 2
		this.initContext("a");
		result = this.expr.parse(this.context);
		assertTrue(result);
		assertEquals(1, this.input.getPosition());

		// test 3
		this.initContext("d");
		result = this.expr.parse(this.context);
		assertTrue(result);
		assertNull(context.popValue());
		assertEquals(1, this.input.getPosition());

		// test 4
		this.initContext("4");
		result = this.expr.parse(this.context);
		assertTrue(result);
		assertNull(context.popValue());
		assertEquals(1, this.input.getPosition());

		// test 5
		this.initContext("ω");
		result = this.expr.parse(this.context);
		assertTrue(result);
		assertNull(context.popValue());
		assertEquals(2, this.input.getPosition());

		// test 6
		this.initContext("あ");
		result = this.expr.parse(this.context);
		assertTrue(result);
		assertNull(context.popValue());
		assertEquals(3, this.input.getPosition());

		// test 7
		String s = new String(Character.toChars(0x21c56));
		this.initContext(s);
		result = this.expr.parse(this.context);
		assertTrue(result);
		assertNull(context.popValue());
		assertEquals(4, this.input.getPosition());

		// failure test
		this.initContext("E");
		result = this.expr.parse(this.context);
		assertTrue(!result);
		assertEquals(0, context.popFailure().getFailurePos());
		assertEquals(0, this.input.getPosition());

		this.initContext("");
		result = this.expr.parse(this.context);
		assertTrue(!result);
		assertEquals(0, context.popFailure().getFailurePos());
		assertEquals(0, this.input.getPosition());
	}
}
