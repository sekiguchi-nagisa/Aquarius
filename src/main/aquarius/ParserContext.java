package aquarius;

import aquarius.CacheFactory.CacheKind;
import aquarius.action.FailedActionException;
import aquarius.expression.ParsingExpression;

public class ParserContext {
	private AquariusInputStream input;
	private ResultCache cache;

	/**
	 * result value of action or capture, or failure. may be null
	 */
	private Object value;

	public ParserContext(AquariusInputStream input) {
		this.input = input;
		this.cache = new CacheFactory(CacheKind.Empty).newCache(0, 0);
	}

	ParserContext(AquariusInputStream input, ResultCache cache) {
		this.input = input;
		this.cache = cache;
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

	@SuppressWarnings("unchecked")
	public <T> T popValue(Class<T> clazz) {
		return (T) this.popValue();
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

	/**
	 * helper method for token text generation
	 * @param token
	 * @return
	 */
	public String createTokenText(Token token) {
		return token.getText(this.input);
	}

	/**
	 * 
	 * @param <R>
	 * @param rule
	 * @return
	 * parsed result of dispatched rule. if match is failed, return Failure
	 */
	<R> boolean dispatchRule(Rule<R> rule) {
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
