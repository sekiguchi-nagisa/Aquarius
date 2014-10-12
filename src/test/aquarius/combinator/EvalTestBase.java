package aquarius.combinator;

import aquarius.combinator.expression.ParsingExpression;
import aquarius.combinator.expression.Rule;
import aquarius.runtime.CommonStream;
import aquarius.runtime.memo.MemoTableFactory;
import aquarius.runtime.memo.NullMemoTableFactory;

/**
 * Base class for EvalTest
 * @author skgchxngsxyz-opensuse
 *
 */
public abstract class EvalTestBase {
	protected CommonStream input;

	protected ParsingExpression expr;

	protected Evaluator evaluator;


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
	 * @param rules
	 * if null, use empty rules
	 */
	protected void initEvaluator(String source, MemoTableFactory factory, Rule[] rules) {
		this.evaluator = new Evaluator(rules == null ? new Rule[]{} : rules);
		this.evaluator.setMemoTableFactory(factory == null ? new NullMemoTableFactory() : factory);
		this.input = new CommonStream("test", source);
		this.evaluator.setInputStream(this.input);
	}

	protected void initEvaluator(String source) {
		this.initEvaluator(source, null, null);
	}
}
