package aquarius.runtime.memo;

import aquarius.runtime.Result;

public class FixedSizeMemoTableFactory implements MemoTableFactory {
	@Override
	public MemoTable newMemoTable(int ruleSize, int srcSize) {
		return new FixedSizeMemoTable(ruleSize, srcSize);
	}

	private static class FixedSizeMemoTable implements MemoTable {
		private final MemoEntry[][] resultArray;

		private FixedSizeMemoTable(int ruleSize, int srcSize) {
			int actualSrcSize = srcSize + 1;
			this.resultArray = new MemoEntry[ruleSize][actualSrcSize];
			for(int i = 0; i < ruleSize; i++) {
				for(int j = 0; j < actualSrcSize; j++) {
					this.resultArray[i][j] = null;
				}
			}
		}

		@Override
		public MemoEntry get(int ruleIndex, int srcPos) {
			return this.resultArray[ruleIndex][srcPos];
		}

		@Override
		public <R> Result<R> set(int ruleIndex, int srcPos, Result<R> result, int currentPos) {
			assert(this.resultArray[ruleIndex][srcPos] != null);
			assert(!(result.isFailure()));

			this.resultArray[ruleIndex][srcPos] = new MemoEntry(currentPos, result);
			return result;
		}
	}
}
