package aquarius.runtime;

import aquarius.runtime.memo.FixedSizeMemoTableFactory;
import aquarius.runtime.memo.MemoTable;
import aquarius.runtime.memo.MemoTableFactory;

public abstract class BaseParser {
	/**
	 * for memo table generation
	 */
	private MemoTableFactory factory = new FixedSizeMemoTableFactory();	// default

	/**
	 * for memoization
	 */
	protected MemoTable memoTable;

	protected AquariusInputStream input;

	public void setMemoTableFactory(MemoTableFactory factory) {
		this.factory = factory;
	}

	public MemoTableFactory getMemoTableFactory() {
		return this.factory;
	}

	/**
	 * set aquarius inout stream and reset parser status
	 * @param input
	 */
	public void setInputStream(AquariusInputStream input) {
		this.reset();
		this.input = input;
	}

	/**
	 * 
	 * @return
	 * return null if input stream is not set
	 */
	public AquariusInputStream getInputStream() {
		return this.input;
	}

	public abstract int getRuleSize();

	/**
	 * reset parser status
	 */
	public void reset() {
		this.input = null;
		this.memoTable = null;
	}

	public ParsedResult parse(int ruleIndex) {
		this.memoTable = this.factory.newMemoTable(this.getRuleSize(), this.input.getInputSize());
		return this.dispatchRule(ruleIndex);
	}

	/**
	 * 
	 * @param ruleIndex
	 * @return
	 * parsed result of dispatched rule. if match is failed, return Failure
	 * @throws IndexOutOfBoundsException
	 * if ruleIndex < 0 or ruleIndex >= BaseParser#getRuleSize()
	 */
	protected abstract ParsedResult dispatchRule(int ruleIndex) throws IndexOutOfBoundsException;
}
