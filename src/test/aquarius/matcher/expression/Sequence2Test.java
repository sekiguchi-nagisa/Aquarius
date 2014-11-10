package aquarius.matcher.expression;

import static aquarius.matcher.Expressions.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.misc.Tuple2;

public class Sequence2Test extends TestBase<Tuple2<Void, Void>> {
	@Before
	public void prepare() {
		this.expr = seq(str("1234"), str("abc"));
		this.initContext("1234abc");
	}

	@Test
	public void test() {
		boolean result = this.expr.parse(this.context);
		assertTrue(result);
		assertEquals(7, this.input.getPosition());

		// failure test
		this.initContext("1234aa");
		result = this.expr.parse(this.context);
		assertTrue(!result);
		assertEquals(4, context.popFailure().getFailurePos());
		assertEquals(0, this.input.getPosition());
	}

}
