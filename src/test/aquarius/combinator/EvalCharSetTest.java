package aquarius.combinator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.runtime.ParsedResult;
import static aquarius.combinator.expression.ParsingExpression.*;

public class EvalCharSetTest extends EvalTestBase {

	@Before
	public void prepare() {
		this.expr = ch('@', '$')._$('a', 'd')._$('3', '5');
		this.initEvaluator("@");
	}

	@Test
	public void test() {
		// test 1
		ParsedResult expected = this.input.createToken(0, 1);
		ParsedResult result = this.expr.accept(this.evaluator);
		assertEquals("mismatch result", expected, result);

		// test 2
		this.initEvaluator("a");
		expected = this.input.createToken(0, 1);
		result = this.expr.accept(this.evaluator);
		assertEquals("mismatch result", expected, result);

		// test 3
		this.initEvaluator("d");
		expected = this.input.createToken(0, 1);
		result = this.expr.accept(this.evaluator);
		assertEquals("mismatch result", expected, result);

		// test 4
		this.initEvaluator("4");
		expected = this.input.createToken(0, 1);
		result = this.expr.accept(this.evaluator);
		assertEquals("mismatch result", expected, result);
	}

}
