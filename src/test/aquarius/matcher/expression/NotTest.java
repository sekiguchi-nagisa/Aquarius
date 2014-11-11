package aquarius.matcher.expression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.misc.Tuple2;
import static aquarius.matcher.Expressions.*;

public class NotTest extends TestBase<Tuple2<Void, Void>> {
	@Before
	public void prepare() {
		this.expr = seq(str("public"), not(oneMore(ch().r('a', 'z').r('A', 'Z').r('0', '9'))));
		this.initContext("public   \t   \t    \t\t");
	}
	@Test
	public void test() {
		boolean result = this.expr.parse(this.context);
		assertEquals(6, this.context.getInputStream().getPosition());
		assertNull(context.popValue());
		assertTrue(result);

		// failure test
		this.initContext("publicd  ");
		result = this.expr.parse(this.context);
		assertTrue(!result);
		assertEquals(6, context.popFailure().getFailurePos());
		assertEquals(0, this.input.getPosition());
	}
}
