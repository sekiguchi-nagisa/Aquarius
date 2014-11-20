package aquarius;


public class CacheEntry {
	private int pos;
	private Object value;

	public CacheEntry(int pos, Object value) {
		this.pos = pos;
		this.value = value;
	}

	public int getCurrentPos() {
		return this.pos;
	}

	public boolean getStatus() {
		return !(value instanceof Failure);
	}

	public Object getValue() {
		return this.value;
	}

	public void reuse(int pos, Object value) {
		this.pos = pos;
		this.value = value;
	}
}