package aquarius.runtime;

/**
 * 
 * @author skgchxngsxyz-opensuse
 *
 */
public interface AquariusInputStream {
	/**
	 * represent for end of file
	 */
	public final static int EOF = -1;

	/**
	 * save current intput position.
	 * @return
	 * current position.
	 */
	public int getPosition();

	/**
	 * change input position
	 * @param position
	 * @throws IndexOutOfBoundsException
	 * if position < 0
	 * if position >= input size
	 */
	public void setPosition(int position) throws IndexOutOfBoundsException;

	/**
	 * get charactor in current position.
	 * @return
	 * consumed character. if input is end of file, return EOF.
	 */

	public int fetch();
	/**
	 * consume character in current position. and increment position.
	 * @return
	 * consumed character. if input is end of file, return EOF.
	 */
	public int consume();

	/**
	 * 
	 * @return
	 * may be null
	 */
	public String getSourceName();

	public void setSourceName(String sourceName);

	/**
	 * create token.
	 * @param startPos
	 * token start position. inclusive.
	 * @param stopPos
	 * token stop position. exclusive.
	 * @return
	 * @throws IndexOutOfBoundsException
	 * if startPos >= stopPos
	 * if startPos < 0
	 * if AquariusInputStream#fetch() == EOF
	 */
	public Token createToken(int startPos, int stopPos) throws IndexOutOfBoundsException;
}
