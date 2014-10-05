package aquarius.runtime;

/**
 * for parsing action enum constant
 * @author skgchxngsxyz-opensuse
 *
 */
public interface ParsingActionSet {
	/**
	 * 
	 * @param argList
	 * the results of preceding expression.
	 * @param index
	 * none negative number.
	 * @return
	 * result of action
	 */
	public ParsedResult action(ResultList argList, int index);

	/**
	 * 
	 * @param argList
	 * the results of preceding expression.
	 * @param index
	 * none negative number
	 * @return
	 * result of action
	 */
	public boolean predicate(ResultList argList, int index);
}
