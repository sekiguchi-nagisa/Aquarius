package aquarius.expression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.misc.Tuple2;
import static aquarius.Expressions.*;

public class AndTest extends TestBase<Tuple2<Void, Void>> {
	@Before
	public void prepare() {
		this.expr = seq(str("public"), and(ch(' ', '\t').oneMore()));
		this.initContext("public   \t   \t    \t\t");
	}
	@Test
	public void test() {
		boolean result = this.expr.parse(this.context);
		this.success(result, 6);
		assertNull(context.popValue());

		// failure test
		this.initContext("public3  ");
		result = this.expr.parse(this.context);
		this.failure(result, 0, 6);
	}
}
