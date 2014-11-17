package aquarius.matcher.expression;

import static aquarius.matcher.Expressions.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class OneMoreTest extends TestBase<List<Void>> {
	@Before
	public void prepare() {
		this.expr = str("hello").oneMore();
		this.initContext("hellohello");
	}

	@Test
	public void test() {
		// test1
		boolean result = this.expr.parse(this.context);
		this.success(result, 10);
		assertNull(context.popValue());

		// test2
		this.initContext("hellos");
		result = this.expr.parse(this.context);
		this.success(result, 5);
		assertNull(context.popValue());

		// failure test
		this.initContext("hells");
		result = this.expr.parse(this.context);
		this.failure(result, 0, 0);
	}
}
