package aquarius.combinator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.runtime.Result;
import aquarius.runtime.Token;
import static aquarius.combinator.expression.ParsingExpression.*;

public class EvalAnyTest extends EvalTestBase<Token> {

	@Before
	public void prepare() {
		this.expr = any();
		this.initContext("G");
	}

	@Test
	public void test() {
		Token expected = this.input.createToken(0, 1);
		Result<Token> result = this.expr.parse(this.context);
		assertEquals("mismatched result", expected, result.get());
	}
}
