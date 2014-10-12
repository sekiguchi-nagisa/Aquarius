package aquarius.combinator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.runtime.ParsedResult;
import static aquarius.combinator.expression.ParsingExpression.*;

public class EvalLiteralTest extends EvalTestBase {
	private final String text = "hello world";
	@Before
	public void prepare() {
		this.expr = str(text);
		this.initContext(text + "hfieur");
	}
	@Test
	public void test() {
		ParsedResult expected = this.input.createToken(0, text.length());
		ParsedResult result = this.expr.parse(this.context);
		assertEquals("mismatched result", expected, result);
	}
}
