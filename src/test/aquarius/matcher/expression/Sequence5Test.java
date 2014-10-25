package aquarius.matcher.expression;

import static aquarius.matcher.expression.ParsingExpression.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.runtime.Result;
import aquarius.runtime.Token;
import aquarius.runtime.Result.Failure;
import aquarius.util.Tuple5;

public class Sequence5Test extends TestBase<Tuple5<Token, Token, Token, Token, Token>> {
	@Before
	public void prepare() {
		this.expr = seq5(str("1234"), str("abc"), any(), ch('a', '2'), any());
		this.initContext("1234abc32@");
	}

	@Test
	public void test() {
		Result<Tuple5<Token, Token, Token, Token, Token>> result = this.expr.parse(this.context);
		assertEquals("1234", result.get().get1().getText(this.input));
		assertEquals("abc", result.get().get2().getText(this.input));
		assertEquals("3", result.get().get3().getText(this.input));
		assertEquals("2", result.get().get4().getText(this.input));
		assertEquals("@", result.get().get5().getText(this.input));
		assertEquals(10, this.input.getPosition());

		// failure test
		this.initContext("1234aa");
		result = this.expr.parse(this.context);
		assertTrue(result.isFailure());
		assertEquals(5, ((Failure<?>) result).getFailurePos());
		assertEquals(0, this.input.getPosition());
	}

}
