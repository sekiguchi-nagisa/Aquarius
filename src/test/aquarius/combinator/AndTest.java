package aquarius.combinator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.runtime.Result;
import aquarius.runtime.Token;
import static aquarius.combinator.expression.ParsingExpression.*;

public class AndTest extends TestBase<Token> {
	@Before
	public void prepare() {
		this.expr = $(str("public"), and(zeroMore(ch(' ', '\t'))));
		this.initContext("public   \t   \t    \t\t");
	}
	@Test
	public void test() {
		Result<Token> result = this.expr.parse(this.context);
		assertEquals("mismatched result", 6, this.context.getInput().getPosition());
		assertEquals("mismatched result", "public", result.get().getText(this.input));
	}
}
