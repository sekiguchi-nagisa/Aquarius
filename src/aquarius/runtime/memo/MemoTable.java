package aquarius.runtime.memo;

import aquarius.runtime.ParsedResult;

/**
 * for memoization
 * @author skgchxngsxyz-opensuse
 *
 */
public interface MemoTable {
	/**
	 * look up previous parsed result
	 * @param ruleIndex
	 * non negative value
	 * @param srcPos
	 * non negative value
	 * @return
	 * return null if not found parsed result
	 */
	public MemoEntry get(int ruleIndex, int srcPos);

	/**
	 * set parsed result.
	 * @param ruleIndex
	 * non negative value
	 * @param srcPos
	 * no negative value
	 * @param result
	 * parsed result. not Failure
	 * @param currentPos
	 * @return
	 * equivalent to result.
	 */
	public ParsedResult set(int ruleIndex, int srcPos, ParsedResult result, int currentPos);

	public static class MemoEntry {
		private final int currentPos;
		private final ParsedResult result;

		protected MemoEntry(int currentPos, ParsedResult result) {
			this.currentPos = currentPos;
			this.result = result;
		}

		public ParsedResult getResult() {
			return this.result;
		}

		public int getCurrentPos() {
			return this.currentPos;
		}
	}
}
