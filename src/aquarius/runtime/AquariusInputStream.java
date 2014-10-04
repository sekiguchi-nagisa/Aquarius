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
	 */
	public void rollbackPosition(int position) throws IndexOutOfBoundsException;

	/**
	 * consume character in current position. and increment position.
	 * @return
	 * consumed character. if input is end of file, return EOF.
	 */
	public int consume();
}
