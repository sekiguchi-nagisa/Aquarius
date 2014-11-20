package aquarius.expression;

import static aquarius.Expressions.*;
import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class OptionalTest extends TestBase<Optional<Void>> {

	@Before
	public void prepare() {
		this.expr = str("hello").opt();
		this.initContext("g");
	}

	@Test
	public void test() {
		// test 1
		boolean result = this.expr.parse(this.context);
		this.success(result, 0);
		assertNull(context.popValue());

		// test 2
		this.initContext("hello");
		result = this.expr.parse(this.context);
		this.success(result, 5);
	}
}
