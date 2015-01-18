package aquarius;

public class CacheEntry {
	private int pos = -1;
	private Object value = null;

	/**
	 * get input position which this entry contains
	 * @return
	 */
	public int getCurrentPos() {
		return this.pos;
	}

	/**
	 * get status
	 * @return
	 */
	public boolean getStatus() {
		return this.pos != -1;
	}

	/**
	 * 
	 * @return
	 * may be null
	 */
	public Object getValue() {
		return this.value;
	}

	/**
	 * reuse this entry for saving memory consumption
	 * @param pos
	 * @param value
	 * may be null
	 */
	public void reuse(int pos, Object value) {
		this.pos = pos;
		this.value = value;
	}
}