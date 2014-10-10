package aquarius.runtime;

/**
 * represent for token
 * @author skgchxngsxyz-opensuse
 *
 */
public interface Token extends ParsedResult {
	/**
	 * get token start position. inclusive
	 * @return
	 * 
	 */
	public int getStartPos();

	/**
	 * get token stop position. exclusive.
	 * @return
	 */
	public int getStopPos();

	/**
	 * get token text size. this is equivalent to Token#getStopPos() - Token#getStartPos()
	 * @return
	 */
	public int getSize();
	/**
	 * get token text. this is equivalent to Token#getSubText(0, 0)
	 * @return
	 */

	/**
	 * get token text. this is equivalent to Token#getSubText(srcInput, 0, 0)
	 * @param srcInput
	 * token source. not null.
	 * @return
	 */
	public default String getText(AquariusInputStream srcInput) {
		return this.getSubText(srcInput, 0, 0);
	}

	/**
	 * get token text. this is equivalent to Token#getText().subString(Token#getStartPos() + startOffset, Token#getStopPos() - stopOffset)
	 * @param srcInput
	 * token source. not null.
	 * @param startOffset
	 * not negative value.
	 * @param stopOffset
	 * not negative value.
	 * @return
	 * @throws IndexOutOfBoundsException
	 * if startOffset < 0 or Token#getStartPos() + startOffset >= Token#getStopPos().
	 * if stopOffset < 0 or Token#getStopPos() - stopOffset < Token#getStartPos().
	 * if Token#getStartPos() + startOffset >= Token#getStopPos() - stopOffset.
	 */
	public String getSubText(AquariusInputStream srcInput, int startOffset, int stopOffset) throws IndexOutOfBoundsException;

	/**
	 * get line number of token
	 * @param srcInput
	 * token source. not null
	 * @return
	 */
	public int getLineNumber(AquariusInputStream srcInput);
}
