package aquarius.matcher.expression;

import static aquarius.matcher.Expressions.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.misc.Tuple5;

public class Sequence5Test extends TestBase<Tuple5<Void, Void, Void, Void, Void>> {
	@Before
	public void prepare() {
		this.expr = seq(str("1234"), str("abc"), ANY, ch('a', '2'), ANY);
		this.initContext("1234abc32@");
	}

	@Test
	public void test() {
		boolean result = this.expr.parse(this.context);
		assertTrue(result);
		assertEquals(10, this.input.getPosition());

		// failure test
		this.initContext("1234aa");
		result = this.expr.parse(this.context);
		assertTrue(!result);
		assertEquals(4, context.popFailure().getFailurePos());
		assertEquals(0, this.input.getPosition());
	}

}
