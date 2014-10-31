package aquarius.matcher.expression;

import static aquarius.matcher.Expressions.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;

import aquarius.runtime.Result;
import aquarius.runtime.Token;

import org.junit.Test;

public class ZeroMoreTest extends TestBase<List<Token>>{
	@Before
	public void prepare() {
		this.expr = zeroMore(ch()._r('a', 'c'));
		this.initContext("8");
	}

	@Test
	public void test() {
		// test 1
		Result<List<Token>> result = this.expr.parse(this.context);
		assertEquals(0, result.get().size());
		assertEquals(0, this.input.getPosition());

		// test 2
		this.initContext("acb");
		result = this.expr.parse(this.context);
		assertEquals(3, result.get().size());
		assertEquals("a", result.get().get(0).getText(this.input));
		assertEquals("c", result.get().get(1).getText(this.input));
		assertEquals("b", result.get().get(2).getText(this.input));
		assertEquals(3, this.input.getPosition());
	}
}
