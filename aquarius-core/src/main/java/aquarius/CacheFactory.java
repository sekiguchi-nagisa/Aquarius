package aquarius;

import java.util.Arrays;

public class CacheFactory {
	public static enum CacheKind {
		Empty, Limit;
	}

	protected final CacheKind kind;

	public CacheFactory(CacheKind kind) {
		this.kind = kind;
	}

	public ResultCache newCache(int ruleSize) {
		switch(this.kind) {
		case Empty:
			return new EmptyCache();
		case Limit:
			return new LimitedSizeCache(ruleSize);
		}
		return null;
	}

	protected static class EmptyCache extends ResultCache {
		@Override
		public CacheEntry get(int ruleIndex, int srcPos) {
			return null;	// always null
		}

		@Override
		public void set(int ruleIndex, int srcPos, Object value, int currentPos) {
		}

		@Override
		public void setFailure(int ruleIndex, int srcPos) {
		}
	}

	protected static class LimitedSizeCache extends ResultCache {
		private EntryRow[] entryTable;

		public LimitedSizeCache(int ruleSize) {
			this.entryTable = new EntryRow[ruleSize];
		}

		@Override
		public CacheEntry get(int ruleIndex, int srcPos) {
			EntryRow row = this.entryTable[ruleIndex];
			return row != null ? row.get(srcPos) : null;
		}

		@Override
		public void set(int ruleIndex, int srcPos, Object value, int currentPos) {
			EntryRow row = this.entryTable[ruleIndex];
			if(row == null) {
				row = new EntryRow();
				this.entryTable[ruleIndex] = row;
			}
			row.set(srcPos, currentPos, value);
		}

		@Override
		public void setFailure(int ruleIndex, int srcPos) {
			EntryRow row = this.entryTable[ruleIndex];
			if(row == null) {
				row = new EntryRow();
				this.entryTable[ruleIndex] = row;
			}
			row.setFailure(srcPos);
		}
	}

	private static class EntryRow {
		private final static int DEFAULT_ROW_SIZE = 16;
		private CacheEntry[] entries;
		private int[] srcIndexEntries;

		public EntryRow() {
			this.entries = new CacheEntry[DEFAULT_ROW_SIZE];
			this.srcIndexEntries = new int[DEFAULT_ROW_SIZE];
			Arrays.fill(this.srcIndexEntries, -1);
		}

		public void set(int srcIndex, int pos, Object value) {
			int index = srcIndex % DEFAULT_ROW_SIZE;
			this.srcIndexEntries[index] = srcIndex;
			CacheEntry entry = this.entries[index];
			if(entry == null || !entry.getStatus()) {
				this.entries[index] = CacheEntry.newCacheEntry(pos, value);
			} else {
				entry.reuse(pos, value);
			}
		}

		public void setFailure(int srcIndex) {
			int index = srcIndex % DEFAULT_ROW_SIZE;
			this.srcIndexEntries[index] = srcIndex;
			this.entries[index] = CacheEntry.FAILURE_ENTRY;
		}

		public CacheEntry get(int srcIndex) {
			int index = srcIndex % DEFAULT_ROW_SIZE;
			if(this.srcIndexEntries[index] == srcIndex) {
				return this.entries[index];
			}
			return null;
		}
	}
}
