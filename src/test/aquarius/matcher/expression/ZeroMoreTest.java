package aquarius.matcher.expression;

import static aquarius.matcher.Expressions.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ZeroMoreTest extends TestBase<List<Void>>{
	@Before
	public void prepare() {
		this.expr = ch().r('a', 'c').zeroMore();
		this.initContext("8");
	}

	@Test
	public void test() {
		// test 1
		boolean result = this.expr.parse(this.context);
		assertNull(context.popValue());
		assertTrue(result);
		assertEquals(0, this.input.getPosition());

		// test 2
		this.initContext("acb");
		result = this.expr.parse(this.context);
		assertTrue(result);
		assertEquals(3, this.input.getPosition());
	}
}
