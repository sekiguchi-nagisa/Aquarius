package aquarius.matcher.expression;

import static aquarius.matcher.Expressions.*;
import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import aquarius.runtime.Token;

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
		assertEquals(0, this.input.getPosition());

		// test 2
		this.initContext("hello");
		Token expected = this.input.createToken(0, 5);
		result = this.expr.parse(this.context);
		assertTrue(result);
		assertEquals(5, this.input.getPosition());
	}

}
