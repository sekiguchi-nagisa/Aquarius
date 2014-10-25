package aquarius.runtime;


/**
 * for memoization
 * @author skgchxngsxyz-opensuse
 *
 */
public interface ResultCache {
	/**
	 * look up previous parsed result
	 * @param ruleIndex
	 * non negative value
	 * @param srcPos
	 * non negative value
	 * @return
	 * return null if not found parsed result
	 */
	public CacheEntry get(int ruleIndex, int srcPos);

	/**
	 * set parsed result.
	 * @param <R>
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
	public <R> Result<R> set(int ruleIndex, int srcPos, Result<R> result, int currentPos);

	public static class CacheEntry {
		private final int currentPos;
		private final Result<?> result;

		protected CacheEntry(int currentPos, Result<?> result) {
			this.currentPos = currentPos;
			this.result = result;
		}

		public Result<?> getResult() {
			return this.result;
		}

		public int getCurrentPos() {
			return this.currentPos;
		}
	}
}