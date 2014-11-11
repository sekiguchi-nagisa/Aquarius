package aquarius.matcher.expression;

import static aquarius.matcher.Expressions.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SequenceTest extends TestBase<Void> {
	@Before
	public void prepare() {
		this.expr = seqN(str("1234"), str("abc"), ANY, ch('a', '2'), ANY);
		this.initContext("1234abc32@");
	}

	@Test
	public void test() {
		boolean result = this.expr.parse(this.context);
		assertTrue(result);
		assertEquals(10, this.input.getPosition());
		assertNull(context.popValue());

		// failure test
		this.initContext("1234aa");
		result = this.expr.parse(this.context);
		assertTrue(!result);
		assertEquals(4, context.popFailure().getFailurePos());
		assertEquals(0, this.input.getPosition());
	}

}
