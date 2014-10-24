package aquarius.matcher.expression;

import static aquarius.matcher.expression.ParsingExpression.*;
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
		assertEquals("mismatched result", 0, result.get().size());

		// test 2
		this.initContext("acb");
		result = this.expr.parse(this.context);
		assertEquals("mismatched result", 3, result.get().size());
		assertEquals("mismatched result", "a", result.get().get(0).getText(this.input));
		assertEquals("mismatched result", "c", result.get().get(1).getText(this.input));
		assertEquals("mismatched result", "b", result.get().get(2).getText(this.input));
	}
}
