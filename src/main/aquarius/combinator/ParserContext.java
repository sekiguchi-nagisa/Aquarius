package aquarius.combinator;

import aquarius.combinator.expression.Rule;
import aquarius.runtime.AquariusInputStream;
import aquarius.runtime.ParsedResult;
import aquarius.runtime.memo.MapBasedMemoTableFactory;
import aquarius.runtime.memo.MemoTable;
import aquarius.runtime.memo.MemoTableFactory;
import aquarius.runtime.memo.MemoTable.MemoEntry;

public class ParserContext {
	private final AquariusInputStream input;
	private final MemoTable memoTable;

	/**
	 * 
	 * @param input
	 * not null
	 * @param factory
	 * not null
	 * @param ruleSize
	 * size of target rules.
	 */
	public ParserContext(AquariusInputStream input, MemoTableFactory factory, int ruleSize) {
		this.input = input;
		this.memoTable = factory.newMemoTable(ruleSize, this.input.getInputSize());
	}

	/**
	 * equivalent to ParserContext(input, new MapBasedMemoTableFactory(), ruleSize)
	 * @param input
	 * not null
	 * @param ruleSize
	 * size of target rules
	 */
	public ParserContext(AquariusInputStream input, int ruleSize) {
		this(input, new MapBasedMemoTableFactory(), ruleSize);
	}

	public AquariusInputStream getInput() {
		return this.input;
	}

	public MemoTable getMemoTable() {
		return this.memoTable;
	}

	/**
	 * 
	 * @param rule
	 * @return
	 * parsed result of dispatched rule. if match is failed, return Failure
	 */
	public ParsedResult dispatchRule(Rule rule) {
		final int ruleIndex = rule.getRuleIndex();
		final int srcPos = this.input.getPosition();

		MemoEntry entry = this.memoTable.get(ruleIndex, srcPos);
		if(entry != null) {
			this.input.setPosition(entry.getCurrentPos());
			return entry.getResult();
		}
		// if not found previous parsed result, invoke rule
		ParsedResult result = rule.getPattern().parse(this);
		return this.memoTable.set(ruleIndex, srcPos, result, this.input.getPosition());
	}
}
