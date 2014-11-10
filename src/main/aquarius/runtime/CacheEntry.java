package aquarius.runtime;

public interface CacheEntry {
	public int getCurrentPos();
	public boolean getStatus();
	public Object getValue();

	public static CacheEntry newEntry(int pos, Object value) {
		if(value == null) {
			return newAlwaysSuccessEntry(pos);
		}
		return new CacheEntry() {
			@Override
			public Object getValue() {
				return value;
			}

			@Override
			public boolean getStatus() {
				return !(value instanceof Failure);
			}

			@Override
			public int getCurrentPos() {
				return pos;
			}
		};
	}

	public static CacheEntry newAlwaysSuccessEntry(int pos) {
		return new CacheEntry() {
			@Override
			public Object getValue() {
				return null;
			}

			@Override
			public boolean getStatus() {
				return true;
			}

			@Override
			public int getCurrentPos() {
				return pos;
			}
		};
	}
}