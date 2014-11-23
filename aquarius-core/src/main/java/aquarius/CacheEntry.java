package aquarius;


public abstract class CacheEntry {
	public abstract int getCurrentPos();
	public abstract boolean getStatus();
	public abstract Object getValue();
	public abstract void reuse(int pos, Object value);

	public final static CacheEntry FAILURE_ENTRY = new CacheEntry() {
		@Override
		public void reuse(int pos, Object value) {
			throw new RuntimeException("unsupported");
		}
		
		@Override
		public Object getValue() {
			throw new RuntimeException("unsupported");
		}
		
		@Override
		public boolean getStatus() {
			return false;
		}
		
		@Override
		public int getCurrentPos() {
			throw new RuntimeException("unsupported");
		}
	};

	public final static CacheEntry newCacheEntry(int pos, Object value) {
		return new CacheEntryImpl(pos, value);
	}

	private static class CacheEntryImpl extends CacheEntry {
		private int pos;
		private Object value;

		public CacheEntryImpl(int pos, Object value) {
			this.pos = pos;
			this.value = value;
		}

		@Override
		public int getCurrentPos() {
			return this.pos;
		}

		@Override
		public boolean getStatus() {
			return true;
		}

		@Override
		public Object getValue() {
			return this.value;
		}

		@Override
		public void reuse(int pos, Object value) {
			this.pos = pos;
			this.value = value;
		}
	}
}