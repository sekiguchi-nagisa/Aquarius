package aquarius.expression;

import static aquarius.Expressions.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ChoiceTest extends TestBase<Void> {
	@Before
	public void prepare() {
		this.expr = choice(str("hello"), str("world"), str("good"));
		this.initContext("hello");
	}

	@Test
	public void test() {
		// test1
		boolean result = this.expr.parse(this.context);
		this.success(result, 5);
		assertNull(context.popValue());

		// test2
		this.initContext("good");
		result = this.expr.parse(this.context);
		this.success(result, 4);
		assertNull(context.popValue());

		// test3
		this.initContext("world");
		result = this.expr.parse(this.context);
		this.success(result, 5);
		assertNull(context.popValue());

		// failure test
		this.initContext("w");
		boolean result2 = this.expr.parse(this.context);
		this.failure(result2, 0, 1);
	}
}
