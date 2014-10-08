package aquarius.runtime.memo;

import aquarius.runtime.ParsedResult;

public class FixedSizeMemoTableFactory implements MemoTableFactory {
	@Override
	public MemoTable newMemoTable(int ruleSize, int srcSize) {
		return new FixedSizeMemoTable(ruleSize, srcSize);
	}

	private static class FixedSizeMemoTable implements MemoTable {
		private final ParsedResult[][] resultArray;

		private FixedSizeMemoTable(int ruleSize, int srcSize) {
			this.resultArray = new ParsedResult[ruleSize][srcSize];
			for(int i = 0; i < ruleSize; i++) {
				for(int j = 0; j < srcSize; j++) {
					this.resultArray[i][j] = null;
				}
			}
		}

		@Override
		public ParsedResult get(int ruleIndex, int srcPos) {
			return this.resultArray[ruleIndex][srcPos];
		}

		@Override
		public ParsedResult set(int ruleIndex, int srcPos, ParsedResult result) {
			assert(this.resultArray[ruleIndex][srcPos] != null);
			return (this.resultArray[ruleIndex][srcPos] = result);
		}
		
	}
}
