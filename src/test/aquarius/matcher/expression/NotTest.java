package aquarius.matcher.expression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.misc.Tuple2;
import static aquarius.matcher.Expressions.*;

public class NotTest extends TestBase<Tuple2<Void, Void>> {
	@Before
	public void prepare() {
		this.expr = seq(str("public"), not(r('a', 'z').r('A', 'Z').r('0', '9').oneMore()));
		this.initContext("public   \t   \t    \t\t");
	}
	@Test
	public void test() {
		boolean result = this.expr.parse(this.context);
		this.success(result, 6);
		assertNull(context.popValue());

		// failure test
		this.initContext("publicd  ");
		result = this.expr.parse(this.context);
		this.failure(result, 0, 6);
	}
}
