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
	public ParsedResult get(int ruleIndex, int srcPos);

	/**
	 * set parsed result.
	 * @param ruleIndex
	 * non negative value
	 * @param srcPos
	 * no negative value
	 * @param result
	 * parsed result. not Failure
	 * @return
	 * equivalent to result.
	 */
	public ParsedResult set(int ruleIndex, int srcPos, ParsedResult result);
}
