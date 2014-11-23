package aquarius.expression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.action.FailedActionException;

public class VoidCustomExprTest extends TestBase<Void> {
	@Before
	public void prepare() {
		VoidCustomExpr c = ctx -> ctx.getInputStream().consume();
		this.expr = c;
		this.initContext("hfreui");
	}
	@Test
	public void test() {
		boolean result = this.expr.parse(this.context);
		this.success(result, 1);
		assertNull(context.popValue());

		// failure test
		VoidCustomExpr c = ctx -> {throw new FailedActionException("fail");};
		this.expr = c;
		this.initContext("12+34");
		result = this.expr.parse(this.context);
		this.failure(result, 0, 0);
	}
}
