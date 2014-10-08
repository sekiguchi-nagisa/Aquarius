package aquarius.runtime.memo;


/**
 * for MemoTable creation
 * @author skgchxngsxyz-opensuse
 *
 */
public interface MemoTableFactory {
	/**
	 * 
	 * @param ruleSize
	 * @param srcSize
	 * @return
	 * new MemoTable instance
	 */
	public MemoTable newMemoTable(int ruleSize, int srcSize);
}
