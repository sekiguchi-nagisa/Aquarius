package aquarius.combinator;

import aquarius.combinator.expression.ParsingExpression;
import aquarius.runtime.CommonStream;
import aquarius.runtime.memo.MemoTableFactory;
import aquarius.runtime.memo.NullMemoTableFactory;

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
	protected void initContext(String source, MemoTableFactory factory, int size) {
		factory = factory == null ? new NullMemoTableFactory() : factory;
		this.input = new CommonStream("test", source);
		this.context = new ParserContext(this.input, factory, size);
	}

	protected void initContext(String source) {
		this.initContext(source, null, 0);
	}
}
