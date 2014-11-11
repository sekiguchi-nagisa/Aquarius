package aquarius.matcher.expression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.matcher.FailedActionException;
import static aquarius.matcher.Expressions.*;

public class NoArgActionTest extends TestBase<Void> {
	@Before
	public void prepare() {
		this.expr = action(ctx -> {ctx.getInputStream().consume(); return null;});
		this.initContext("hfreui");
	}
	@Test
	public void test() {
		boolean result = this.expr.parse(this.context);
		assertTrue(result);
		assertNull(context.popValue());
		assertEquals(1, this.input.getPosition());

		// failure test
		this.expr = action(ctx -> {throw new FailedActionException("fail");});
		this.initContext("12+34");
		result = this.expr.parse(this.context);
		assertTrue(!result);
		assertEquals(0, context.popFailure().getFailurePos());
		assertEquals(0, this.input.getPosition());
	}
}
