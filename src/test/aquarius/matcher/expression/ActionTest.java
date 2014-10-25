package aquarius.matcher.expression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.matcher.FailedActionException;
import aquarius.runtime.Result;
import aquarius.runtime.Result.Failure;
import static aquarius.matcher.expression.ParsingExpression.*;

public class ActionTest extends TestBase<Integer> {
	@Before
	public void prepare() {
		this.expr = action(seq(str("12"), ch('+'), str("34")), 
				(ctx, a) -> {
					int left = Integer.parseInt(a.get(0).getText(input));
					int right = Integer.parseInt(a.get(2).getText(input));
					return Result.of(left + right);
				});
		this.initContext("12+34");
	}
	@Test
	public void test() {
		Result<Integer> result = this.expr.parse(this.context);
		assertEquals(46, result.get().intValue());
		assertEquals(5, this.input.getPosition());

		// failure test
		this.expr = action(seq(str("12"), ch('+'), str("34")), 
				(ctx, a) -> {
					throw new FailedActionException("action failed");
				});
		this.initContext("12+34");
		result = this.expr.parse(this.context);
		assertTrue(result.isFailure());
		assertEquals(5, ((Failure<?>) result).getFailurePos());
		assertEquals(0, this.input.getPosition());
	}
}
