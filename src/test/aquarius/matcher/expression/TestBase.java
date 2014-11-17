package aquarius.matcher.expression;

import static org.junit.Assert.*;

import aquarius.matcher.Grammar;
import aquarius.matcher.ParserContext;
import aquarius.matcher.expression.ParsingExpression;
import aquarius.runtime.CacheFactory;
import aquarius.runtime.CommonStream;
import aquarius.runtime.CacheFactory.CacheKind;

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


	/**
	 * init input and evaluator
	 * @param source
	 * @param rules
	 */

	/**
	 * nit input and evaluator
	 * @param source
	 * not null
	 * @param factory
	 * if null, use NullMemoFactory.
	 * @param size
	 */
	protected void initContext(String source, CacheFactory factory) {
		factory = factory == null ? new CacheFactory(CacheKind.Empty) : factory;
		this.input = new CommonStream("test", source);
		this.context = new ParserContext(new Grammar() {}, this.input, factory);
	}

	protected void initContext(String source) {
		this.initContext(source, null);
	}

	protected void success(boolean status, int position) {
		assertTrue(status);
		assertEquals(position, this.input.getPosition());
	}

	protected void failure(boolean status, int position, int failruePos) {
		assertTrue(!status);
		assertEquals(position, this.input.getPosition());
		assertEquals(failruePos, this.context.popFailure().getFailurePos());
	}
}
