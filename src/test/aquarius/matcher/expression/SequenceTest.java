package aquarius.matcher.expression;

import static aquarius.matcher.Expressions.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SequenceTest extends TestBase<Void> {
	@Before
	public void prepare() {
		this.expr = seqN(str("1234"), str("abc"), ANY, ch('a', '2'), ANY);
		this.initContext("1234abc32@");
	}

	@Test
	public void test() {
		boolean result = this.expr.parse(this.context);
		this.success(result, 10);
		assertNull(context.popValue());

		// failure test
		this.initContext("1234aa");
		result = this.expr.parse(this.context);
		this.failure(result, 0, 4);
	}
}
