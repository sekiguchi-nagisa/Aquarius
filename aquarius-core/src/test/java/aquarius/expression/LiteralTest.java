package aquarius.expression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import static aquarius.Expressions.*;

public class LiteralTest extends TestBase<Void> {
	@Before
	public void prepare() {
		this.expr = str("hello world");
		this.initContext("hello worldhfieur");
	}
	@Test
	public void test() {
		// test1
		boolean result = this.expr.parse(this.context);
		this.success(result, 11);
		assertNull(context.popValue());

		// test2
		this.expr = str("aこ12");
		this.initContext("aこ12hur");
		result = this.expr.parse(this.context);
		this.success(result, 6);
		assertNull(context.popValue());

		// failure test
		this.initContext("hellod world");
		this.expr = str("hello world");
		result = this.expr.parse(this.context);
		this.failure(result, 0, 5);
	}
}
