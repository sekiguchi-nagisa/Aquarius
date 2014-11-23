package aquarius;

import aquarius.CacheFactory.CacheKind;
import aquarius.Parser.PatternWrapper;
import aquarius.expression.ParsingExpression;

public class Rule<R> implements ParsingExpression<R> {
	/**
	 * for memoization
	 */
	private final int ruleIndex;

	/**
	 * for initialization of memo
	 */
	private int ruleSize;

	private final boolean returnable;
	/**
	 * will be null after call initExpr()
	 */
	private PatternWrapper<R> wrapper;

	private ParsingExpression<R> pattern;

	public Rule(int ruleIndex, PatternWrapper<R> wrapper, boolean returnable) {
		this.ruleIndex = ruleIndex;
		this.wrapper = wrapper;
		this.returnable = returnable;
	}

	@Override
	public <T> T accept(ExpressionVisitor<T> visitor) {
		return visitor.visitRule(this);
	}

	@Override
	public boolean parse(ParserContext context) {
		return context.dispatchRule(this);
	}

	@Override
	public boolean isReturnable() {
		return this.returnable;
	}

	void init(int ruleSize) {
		this.ruleSize = ruleSize;
		this.pattern = this.wrapper.invoke();
		this.wrapper = null;
	}

	public int getRuleIndex() {
		return this.ruleIndex;
	}

	public ParsingExpression<R> getPattern() {
		return this.pattern;
	}

	// parser entry point
	public ParsedResult<R> parse(AquariusInputStream input) {
		return this.parse(input, new CacheFactory(CacheKind.Limit));
	}

	public ParsedResult<R> parse(AquariusInputStream input, CacheFactory factory) {
		ParserContext context = new ParserContext(input, factory.newCache(this.ruleSize, 0));

		// start parsing
		boolean status = this.parse(context);

		// create result
		return new ParsedResult<>(status ? context.popValue() : context.popFailure());
	}
}
