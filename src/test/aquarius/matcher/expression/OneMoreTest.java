package aquarius.matcher.expression;

import static aquarius.matcher.Expressions.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class OneMoreTest extends TestBase<List<Void>> {
	@Before
	public void prepare() {
		this.expr = oneMore(str("hello"));
		this.initContext("hellohello");
	}

	@Test
	public void test() {
		// test1
		String expectedText = "hello";
		boolean result = this.expr.parse(this.context);
//		assertEquals(2, result.get().size());
//		assertEquals(expectedText, result.get().get(0).getText(this.input));
//		assertEquals(expectedText, result.get().get(1).getText(this.input));
		assertTrue(result);
		assertEquals(10, this.input.getPosition());

		// test2
		this.initContext("hellos");
		result = this.expr.parse(this.context);
//		assertEquals(1, result.get().size());
//		assertEquals(expectedText, result.get().get(0).getText(this.input));
		assertTrue(result);
		assertEquals(5, this.input.getPosition());

		// failure test
		this.initContext("hells");
		result = this.expr.parse(this.context);
		assertTrue(!result);
		assertEquals(0, context.popFailure().getFailurePos());
		assertEquals(0, this.input.getPosition());
	}
}
