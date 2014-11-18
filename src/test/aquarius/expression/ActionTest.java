package aquarius.expression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.action.FailedActionException;
import static aquarius.Expressions.*;

public class ActionTest extends TestBase<Integer> {
	@Before
	public void prepare() {
		this.expr = ANY.action((ctx, a) -> 12);
		this.initContext("hfreui");
	}
	@Test
	public void test() {
		boolean result = this.expr.parse(this.context);
		this.success(result, 1);
		assertEquals(12, this.context.popValue(Integer.class).intValue());

		// failure test
		this.expr = ANY.action((ctx, a) -> {throw new FailedActionException("fail");});
		this.initContext("12+34");
		result = this.expr.parse(this.context);
		this.failure(result, 1, 1);
	}
}
