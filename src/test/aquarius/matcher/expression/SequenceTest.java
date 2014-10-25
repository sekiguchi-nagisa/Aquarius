package aquarius.matcher.expression;

import static aquarius.matcher.expression.ParsingExpression.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import aquarius.runtime.Result;
import aquarius.runtime.Token;
import aquarius.runtime.Result.Failure;

public class SequenceTest extends TestBase<List<Token>> {
	@Before
	public void prepare() {
		this.expr = seq(ch('\t', ' '), str("hello"), any(), str("world"));
		this.initContext("\thello world");
	}

	@Test
	public void test() {
		Result<List<Token>> result = this.expr.parse(this.context);
		assertEquals(4,       result.get().size());
		assertEquals("\t",    result.get().get(0).getText(this.input));
		assertEquals("hello", result.get().get(1).getText(this.input));
		assertEquals(" ",     result.get().get(2).getText(this.input));
		assertEquals("world", result.get().get(3).getText(this.input));
		assertEquals(12, this.input.getPosition());

		// failure test
		this.initContext("\thello w");
		result = this.expr.parse(this.context);
		assertTrue(result.isFailure());
		assertEquals(8, ((Failure<?>) result).getFailurePos());
		assertEquals(0, this.input.getPosition());
	}

}
