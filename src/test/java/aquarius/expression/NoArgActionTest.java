package aquarius.expression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.action.FailedActionException;
import static aquarius.Expressions.*;

public class NoArgActionTest extends TestBase<Void> {
	@Before
	public void prepare() {
		this.expr = actionNoRet(ctx -> ctx.getInputStream().consume());
		this.initContext("hfreui");
	}
	@Test
	public void test() {
		boolean result = this.expr.parse(this.context);
		this.success(result, 1);
		assertNull(context.popValue());

		// failure test
		this.expr = actionNoRet(ctx -> {throw new FailedActionException("fail");});
		this.initContext("12+34");
		result = this.expr.parse(this.context);
		this.failure(result, 0, 0);
	}
}
