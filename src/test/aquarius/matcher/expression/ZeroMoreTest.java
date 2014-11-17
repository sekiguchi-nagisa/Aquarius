package aquarius.matcher.expression;

import static aquarius.matcher.Expressions.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ZeroMoreTest extends TestBase<List<Void>>{
	@Before
	public void prepare() {
		this.expr = r('a', 'c').zeroMore();
		this.initContext("8");
	}

	@Test
	public void test() {
		// test 1
		boolean result = this.expr.parse(this.context);
		assertNull(context.popValue());
		this.success(result, 0);

		// test 2
		this.initContext("acb");
		result = this.expr.parse(this.context);
		this.success(result, 3);
	}
}
