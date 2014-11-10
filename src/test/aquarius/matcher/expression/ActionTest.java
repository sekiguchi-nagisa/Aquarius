package aquarius.matcher.expression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.matcher.FailedActionException;
import static aquarius.matcher.Expressions.*;

public class ActionTest extends TestBase<Integer> {
	@Before
	public void prepare() {
		this.expr = ANY.action((ctx, a) -> 12);
		this.initContext("hfreui");
	}
	@Test
	public void test() {
		boolean result = this.expr.parse(this.context);
		assertTrue(result);
		assertEquals(12, this.context.popValue());
		assertEquals(1, this.input.getPosition());

		// failure test
		this.expr = ANY.action((ctx, a) -> {throw new FailedActionException("fail");});
		this.initContext("12+34");
		result = this.expr.parse(this.context);
		assertTrue(!result);
		assertEquals(1, context.popFailure().getFailurePos());
		assertEquals(1, this.input.getPosition());
	}
}
