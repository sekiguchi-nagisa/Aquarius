package aquarius.combinator;

import static aquarius.combinator.expression.ParsingExpression.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.runtime.Result;
import aquarius.runtime.Token;

public class Capture extends TestBase<Token> {
	@Before
	public void prepare() {
		this.expr = $(str("1"), ch()._r('a', 'z'), any(), ch('A', 'Z', 'E'));
		this.initContext("1f4Z");
	}

	@Test
	public void test() {
		Token expectedToken = this.input.createToken(0, 4);
		Result<Token> result = this.expr.parse(this.context);
		assertEquals("mismatched result", expectedToken, result.get());
	}
}
