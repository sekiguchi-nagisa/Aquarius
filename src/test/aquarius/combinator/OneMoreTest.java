package aquarius.combinator;

import static aquarius.combinator.expression.ParsingExpression.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;

import aquarius.runtime.Result;
import aquarius.runtime.Token;

import org.junit.Test;

public class OneMoreTest extends TestBase<List<Token>>{
	@Before
	public void prepare() {
		this.expr = oneMore(str("hello"));
		this.initContext("hellohello");
	}

	@Test
	public void test() {
		String expectedText = "hello";
		Result<List<Token>> result = this.expr.parse(this.context);
		assertEquals("mismatched result", 2, result.get().size());
		assertEquals("mismatched result", expectedText, result.get().get(0).getText(this.input));
		assertEquals("mismatched result", expectedText, result.get().get(1).getText(this.input));
	}
}
