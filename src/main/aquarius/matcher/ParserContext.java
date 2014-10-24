package aquarius.matcher;

import aquarius.matcher.Grammar.Rule;
import aquarius.runtime.AquariusInputStream;
import aquarius.runtime.CacheFactory;
import aquarius.runtime.Result;
import aquarius.runtime.ResultCache;
import aquarius.runtime.CacheFactory.CacheKind;
import aquarius.runtime.ResultCache.CacheEntry;

public class ParserContext {
	private final AquariusInputStream input;
	private final ResultCache cache;
	private final Grammar grammar;

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
		this(grammar, input, new CacheFactory(CacheKind.Map));
	}

	public AquariusInputStream getInputStream() {
		return this.input;
	}

	public ResultCache getCache() {
		return this.cache;
	}

	public <R> Result<R> parse(Rule<R> startRule) {
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
	@SuppressWarnings("unchecked")
	public <R> Result<R> dispatchRule(Rule<R> rule) {
		final int ruleIndex = rule.getRuleIndex();
		final int srcPos = this.input.getPosition();

		CacheEntry entry = this.cache.get(ruleIndex, srcPos);
		if(entry != null) {
			this.input.setPosition(entry.getCurrentPos());
			return (Result<R>) entry.getResult();
		}
		// if not found previous parsed result, invoke rule
		Result<R> result = rule.getPattern().parse(this);
		return this.cache.set(ruleIndex, srcPos, result, this.input.getPosition());
	}
}
