package aquarius.expression;

import static aquarius.Expressions.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.misc.Tuple3;

public class Sequence3Test extends TestBase<Tuple3<Void, Void, Void>> {
	@Before
	public void prepare() {
		this.expr = seq(str("1234"), str("abc"), ANY);
		this.initContext("1234abc3");
	}

	@Test
	public void test() {
		boolean result = this.expr.parse(this.context);
		this.success(result, 8);
		assertNull(context.popValue());

		// failure test
		this.initContext("1234aa");
		result = this.expr.parse(this.context);
		this.failure(result, 0, 5);
	}
}
