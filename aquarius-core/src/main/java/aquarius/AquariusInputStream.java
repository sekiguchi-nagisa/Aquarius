package aquarius;

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
	 * get current input position.
	 * @return
	 * current position.
	 * pos > -1 && pos <= getInputSize()
	 */
	public int getPosition();

	/**
	 * change input position
	 * @param position
	 * @throws IndexOutOfBoundsException
	 * if position < 0
	 * if position > input size
	 */
	public void setPosition(int position) throws IndexOutOfBoundsException;

	/**
	 * get character in current position.
	 * @return
	 * consumed character. if input is end of file, return EOF.
	 */
	public int fetch();

	/**
	 * consume character in current position and increment position.
	 */
	public void consume();

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
	 * @param length
	 * token length. may be 0
	 * @return
	 * @throws IndexOutOfBoundsException
	 * if startPos < 0, startPos > getInputSize()
	 * if length < 0
	 */
	public Token createToken(int startPos, int length) throws IndexOutOfBoundsException;

	/**
	 * create token. it is equivalent to createToken(startPos, getPosition() - startPos)
	 * @param startPos
	 * token start position. inclusive.
	 * @return
	 * @throws IndexOutOfBoundsException
	 * if startPos > getPosition()
	 * if startPos < 0
	 */
	public default Token createToken(int startPos) throws IndexOutOfBoundsException {
		return this.createToken(startPos, this.getPosition() - startPos);
	}
}
