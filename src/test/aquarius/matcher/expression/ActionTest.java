package aquarius.matcher.expression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aquarius.runtime.Result;
import static aquarius.matcher.expression.ParsingExpression.*;

public class ActionTest extends TestBase<Integer> {
	@Before
	public void prepare() {
		this.expr = action(seq(str("12"), ch('+'), str("34")), 
				a -> {
					int left = Integer.parseInt(a.get(0).getText(input));
					int right = Integer.parseInt(a.get(2).getText(input));
					return Result.of(left + right);
				});
		this.initContext("12+34");
	}
	@Test
	public void test() {
		Result<Integer> result = this.expr.parse(this.context);
		assertEquals("mismatched result", 46, result.get().intValue());
	}
}
