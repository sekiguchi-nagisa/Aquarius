package aquarius.expression;

import static aquarius.Expressions.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.misc.Tuple4;

public class Sequence4Test extends TestBase<Tuple4<Void, Void, Void, Void>> {
	@Before
	public void prepare() {
		this.expr = seq(str("1234"), str("abc"), ANY, ch('a', '2'));
		this.initContext("1234abc32");
	}

	@Test
	public void test() {
		boolean result = this.expr.parse(this.context);
		this.success(result, 9);
		assertNull(context.popValue());

		// failure test
		this.initContext("1234aa");
		result = this.expr.parse(this.context);
		this.failure(result, 0, 5);
	}
}
