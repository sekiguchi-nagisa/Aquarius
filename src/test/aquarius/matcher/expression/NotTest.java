package aquarius.matcher.expression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.runtime.Result;
import aquarius.runtime.Token;
import aquarius.runtime.Result.Failure;
import static aquarius.matcher.expression.ParsingExpression.*;

public class NotTest extends TestBase<Token> {
	@Before
	public void prepare() {
		this.expr = $(str("public"), not(oneMore(ch()._r('a', 'z')._r('A', 'Z')._r('0', '9'))));
		this.initContext("public   \t   \t    \t\t");
	}
	@Test
	public void test() {
		Result<Token> result = this.expr.parse(this.context);
		assertEquals(6, this.context.getInputStream().getPosition());
		assertEquals("public", result.get().getText(this.input));

		// failure test
		this.initContext("publicd  ");
		result = this.expr.parse(this.context);
		assertTrue(result.isFailure());
		assertEquals(6, ((Failure<?>) result).getFailurePos());
		assertEquals(0, this.input.getPosition());
	}
}
