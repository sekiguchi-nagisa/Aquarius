package aquarius.matcher.expression;

import static aquarius.matcher.Expressions.*;
import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class OptionalTest extends TestBase<Optional<Void>> {

	@Before
	public void prepare() {
		this.expr = opt(str("hello"));
		this.initContext("g");
	}

	@Test
	public void test() {
		// test 1
		boolean result = this.expr.parse(this.context);
		assertTrue(result);
		assertNull(context.popValue());
		assertEquals(0, this.input.getPosition());

		// test 2
		this.initContext("hello");
		result = this.expr.parse(this.context);
		assertTrue(result);
		assertEquals(5, this.input.getPosition());
	}

}
