package aquarius.runtime;


/**
 * for memoization
 * @author skgchxngsxyz-opensuse
 *
 */
public abstract class ResultCache {
	/**
	 * look up previous parsed result
	 * @param ruleIndex
	 * non negative value
	 * @param srcPos
	 * non negative value
	 * @return
	 * return null if not found parsed result
	 */
	public abstract CacheEntry get(int ruleIndex, int srcPos);

	/**
	 * set parsed result.
	 * @param ruleIndex
	 * non negative value
	 * @param srcPos
	 * no negative value
	 * @param value
	 * parsed result
	 * @param currentPos
	 */
	public abstract void set(int ruleIndex, int srcPos, Object value, int currentPos);
}
