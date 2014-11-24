package aquarius.expression;

import static org.junit.Assert.*;
import aquarius.CommonStream;
import aquarius.ParserContext;
import aquarius.expression.ParsingExpression;

/**
 * Base class for EvalTest
 * @author skgchxngsxyz-opensuse
 * @param <R>
 * @param <R>
 *
 */
public abstract class TestBase<R> {
	protected CommonStream input;

	protected ParsingExpression<R> expr;

	protected ParserContext context;

	protected void initContext(String source) {
		this.input = new CommonStream("test", source);
		this.context = new ParserContext(this.input);
	}

	protected void success(boolean status, int position) {
		assertTrue(status);
		assertEquals(position, this.input.getPosition());
	}

	protected void failure(boolean status, int position, int failruePos) {
		assertTrue(!status);
		assertEquals(position, this.input.getPosition());
		assertEquals(failruePos, this.context.getFailure().getFailurePos());
	}
}
