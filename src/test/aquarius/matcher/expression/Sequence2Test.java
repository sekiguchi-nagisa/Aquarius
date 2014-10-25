package aquarius.matcher.expression;

import static aquarius.matcher.expression.ParsingExpression.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.runtime.Result;
import aquarius.runtime.Token;
import aquarius.runtime.Result.Failure;
import aquarius.util.Tuple2;

public class Sequence2Test extends TestBase<Tuple2<Token, Token>> {
	@Before
	public void prepare() {
		this.expr = seq2(str("1234"), str("abc"));
		this.initContext("1234abc");
	}

	@Test
	public void test() {
		Result<Tuple2<Token, Token>> result = this.expr.parse(this.context);
		assertEquals("1234", result.get().get1().getText(this.input));
		assertEquals("abc", result.get().get2().getText(this.input));
		assertEquals(7, this.input.getPosition());

		// failure test
		this.initContext("1234aa");
		result = this.expr.parse(this.context);
		assertTrue(result.isFailure());
		assertEquals(5, ((Failure<?>) result).getFailurePos());
		assertEquals(0, this.input.getPosition());
	}

}
