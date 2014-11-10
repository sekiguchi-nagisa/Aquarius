package aquarius.matcher;

import aquarius.matcher.Grammar.Rule;
import aquarius.matcher.expression.ParsingExpression;
import aquarius.runtime.AquariusInputStream;
import aquarius.runtime.CacheEntry;
import aquarius.runtime.CacheFactory;
import aquarius.runtime.Failure;
import aquarius.runtime.ResultCache;
import aquarius.runtime.CacheFactory.CacheKind;

public class ParserContext {
	private final AquariusInputStream input;
	private final ResultCache cache;
	private final Grammar grammar;

	/**
	 * result value of action or capture, or failure. may be null
	 */
	private Object value;

	/**
	 * 
	 * @param input
	 * not null
	 * @param factory
	 * not null
	 * @param ruleSize
	 * size of target rules.
	 */
	public ParserContext(Grammar grammar, AquariusInputStream input, CacheFactory factory) {
		this.input = input;
		this.cache = factory.newCache(grammar.getIndexSize(), this.input.getInputSize());
		this.grammar = grammar;
	}

	/**
	 * equivalent to ParserContext(input, new MapBasedMemoTableFactory(), ruleSize)
	 * @param input
	 * not null
	 * @param ruleSize
	 * size of target rules
	 */
	public ParserContext(Grammar grammar, AquariusInputStream input) {
		this(grammar, input, new CacheFactory(CacheKind.Limit));
	}

	public AquariusInputStream getInputStream() {
		return this.input;
	}

	public ResultCache getCache() {
		return this.cache;
	}

	public void pushValue(Object value) {
		this.value = value;
	}

	/**
	 * remove special result of preceding expression
	 * @return
	 * removed value. may be null
	 */
	public Object popValue() {
		Object value = this.value;
		this.value = null;
		return value;
	}

	public void pushFailure(int pos, FailedActionException e) {
		this.value = Failure.failInAction(pos, e);
	}

	public void pushFailure(int pos, ParsingExpression<?> expr) {
		this.value = Failure.failInExpr(pos, expr);
	}

	public void pushFailure(Failure failure) {
		assert failure != null;
		this.value = failure;
	}

	/**
	 * 
	 * @return
	 * may be null
	 */
	public Failure popFailure() {
		assert this.value != null;
		Failure failure = (Failure) this.value;
		this.value = null;
		return failure;
	}

	public <R> boolean parse(Rule<R> startRule) {
		if(!startRule.equals(this.grammar.getRule(startRule.getRuleName()))) {
			throw new IllegalArgumentException("grammar not include this rule: " + startRule);
		}
		return startRule.parse(this);
	}

	/**
	 * 
	 * @param <R>
	 * @param rule
	 * @return
	 * parsed result of dispatched rule. if match is failed, return Failure
	 */
	public <R> boolean dispatchRule(Rule<R> rule) {
		final int ruleIndex = rule.getRuleIndex();
		final int srcPos = this.input.getPosition();

		CacheEntry entry = this.cache.get(ruleIndex, srcPos);
		if(entry != null) {
			this.input.setPosition(entry.getCurrentPos());
			this.value = entry.getValue();
			return entry.getStatus();
		}
		// if not found previous parsed result, invoke rule
		boolean status = rule.getPattern().parse(this);
		this.cache.set(ruleIndex, srcPos, this.value, this.input.getPosition());
		return status;
	}
}
