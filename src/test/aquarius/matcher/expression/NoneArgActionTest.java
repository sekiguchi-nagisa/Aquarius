package aquarius.matcher.expression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.matcher.FailedActionException;
import aquarius.runtime.Result;
import aquarius.runtime.Result.Failure;
import static aquarius.matcher.Expressions.*;

public class NoneArgActionTest extends TestBase<Integer> {
	@Before
	public void prepare() {
		this.expr = action(ctx -> 12);
		this.initContext("hfreui");
	}
	@Test
	public void test() {
		Result<Integer> result = this.expr.parse(this.context);
		assertEquals(12, result.get().intValue());
		assertEquals(0, this.input.getPosition());

		// failure test
		this.expr = action(ctx -> {throw new FailedActionException("fail");});
		this.initContext("12+34");
		result = this.expr.parse(this.context);
		assertTrue(result.isFailure());
		assertEquals(0, ((Failure<?>) result).getFailurePos());
		assertEquals(0, this.input.getPosition());
	}
}
