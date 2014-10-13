package aquarius.runtime.memo;

import aquarius.runtime.Result;

/**
 * non memoization
 * @author skgchxngsxyz-opensuse
 *
 */
public class NullMemoTableFactory implements MemoTableFactory {
	@Override
	public MemoTable newMemoTable(int ruleSize, int srcSize) {
		return new NullMemoTable();
	}

	private static class NullMemoTable implements MemoTable {
		@Override
		public MemoEntry get(int ruleIndex, int srcPos) {
			return null;	// always null
		}

		@Override
		public <R> Result<R> set(int ruleIndex, int srcPos, Result<R> result, int currentPos) {
			return result;
		}
	}
}
