package aquarius.combinator;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.runtime.ParsedResult;
import static aquarius.combinator.expression.ParsingExpression.*;

public class EvalAnyTest extends EvalTestBase {

	@Before
	public void prepare() {
		this.expr = any();
		this.initContext("G");
	}

	@Test
	public void test() {
		ParsedResult expected = this.input.createToken(0, 1);
		ParsedResult result = this.expr.parse(this.context);
		assertEquals("mismatched result", expected, result);
	}
}
