package aquarius.runtime;

/**
 * represent for token
 * @author skgchxngsxyz-opensuse
 *
 */
public interface Token {
	/**
	 * get token start position. inclusive
	 * @return
	 * 
	 */
	public int getStartPos();

	/**
	 * get token text size.
	 * @return
	 */
	public int getSize();

	/**
	 * get token text.
	 * @param srcInput
	 * token source. not null.
	 * @return
	 */
	public String getText(AquariusInputStream srcInput);

	/**
	 * get line number of token
	 * @param srcInput
	 * token source. not null
	 * @return
	 */
	public int getLineNumber(AquariusInputStream srcInput);

	/**
	 * get position in line.
	 * @param srcInput
	 * not nul;
	 * @return
	 */
	public int getPosInLine(AquariusInputStream srcInput);
}
