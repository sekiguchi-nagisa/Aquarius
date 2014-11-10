package aquarius.matcher.expression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.misc.Tuple2;
import static aquarius.matcher.Expressions.*;

public class PredictActionTest extends TestBase<Tuple2<Void, Void>> {
	@Before
	public void prepare() {
		this.expr = seq(str("public"), predict(ctx -> true));
		this.initContext("public ");
	}
	@Test
	public void test() {
		boolean result = this.expr.parse(this.context);
		assertEquals(6, this.context.getInputStream().getPosition());
		assertTrue(result);

		// failure test
		this.expr = seq(str("public"), predict(ctx -> false));
		this.initContext("public3  ");
		result = this.expr.parse(this.context);
		assertTrue(!result);
		assertEquals(0, this.input.getPosition());
		assertEquals(6, context.popFailure().getFailurePos());
	}
}
