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
	 * @param ruleIndex
	 * non negative value
	 * @param srcPos
	 * no negative value
	 * @param value
	 * parsed result
	 * @param currentPos
	 */
	public void set(int ruleIndex, int srcPos, Object value, int currentPos);
}
