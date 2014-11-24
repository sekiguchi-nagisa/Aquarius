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

	/**
	 * longest matched failure. may be null if has no failure.
	 */
	private final Failure longestFailure = new Failure();

	private boolean failureCreation = true;

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

	/**
	 * 
	 * @param failureCreation
	 * if true, enable failure creation.
	 */
	public void setFailureCreation(boolean failureCreation) {
		this.failureCreation = failureCreation;
	}

	public void pushFailure(int failurePos, FailedActionException e) {
		if(this.checkFailureCreation(failurePos)) {
			this.longestFailure.reuse(failurePos, e);
		}
	}

	public void pushFailure(int failurePos, ParsingExpression<?> expr) {
		if(this.checkFailureCreation(failurePos)) {
			this.longestFailure.reuse(failurePos, expr);
		}
	}

	private final boolean checkFailureCreation(int failurePos) {
		return this.failureCreation && (this.longestFailure == null 
				|| failurePos > this.longestFailure.getFailurePos());
	}

	/**
	 * 
	 * @return
	 * may be null
	 */
	public Failure getFailure() {
		return this.longestFailure;
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
			if(!entry.getStatus()) {
				this.value = null;
				return false;
			}
			this.input.setPosition(entry.getCurrentPos());
			this.value = entry.getValue();
			return true;
		}
		// if not found previous parsed result, invoke rule
		boolean status = rule.getPattern().parse(this);
		if(status) {
			this.cache.set(ruleIndex, srcPos, this.value, this.input.getPosition());
		} else {
			this.cache.setFailure(ruleIndex, srcPos);
		}
		return status;
	}
}
