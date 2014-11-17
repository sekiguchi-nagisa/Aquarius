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
		this.success(result, 6);
		assertNull(context.popValue());

		// failure test
		this.expr = seq(str("public"), predict(ctx -> false));
		this.initContext("public3  ");
		result = this.expr.parse(this.context);
		this.failure(result, 0, 6);
	}
}
