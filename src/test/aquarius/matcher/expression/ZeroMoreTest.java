package aquarius.matcher.expression;

import static aquarius.matcher.Expressions.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;

import aquarius.runtime.Token;

import org.junit.Test;

public class ZeroMoreTest extends TestBase<List<Void>>{
	@Before
	public void prepare() {
		this.expr = zeroMore(ch().r('a', 'c'));
		this.initContext("8");
	}

	@Test
	public void test() {
		// test 1
		boolean result = this.expr.parse(this.context);
//		assertEquals(0, result.get().size());
		assertTrue(result);
		assertEquals(0, this.input.getPosition());

		// test 2
		this.initContext("acb");
		result = this.expr.parse(this.context);
//		assertEquals(3, result.get().size());
//		assertEquals("a", result.get().get(0).getText(this.input));
//		assertEquals("c", result.get().get(1).getText(this.input));
//		assertEquals("b", result.get().get(2).getText(this.input));
		assertTrue(result);
		assertEquals(3, this.input.getPosition());
	}
}
