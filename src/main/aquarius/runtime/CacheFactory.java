package aquarius.runtime;

import java.util.HashMap;

public class CacheFactory {
	public static enum CacheKind {
		Empty, Array, Map, Limit;
	}

	protected final CacheKind kind;

	public CacheFactory(CacheKind kind) {
		this.kind = kind;
	}

	public ResultCache newCache(int ruleSize, int srcSize) {
		switch(this.kind) {
		case Array:
			return new ArrayBasedCache(ruleSize, srcSize);
		case Empty:
			return new EmptyCache();
		case Map:
			return new MapBasedCache(16);
		case Limit:
			return new LimitedSizeCache(ruleSize, srcSize);
		}
		return null;
	}

	protected static class EmptyCache implements ResultCache {
		@Override
		public CacheEntry get(int ruleIndex, int srcPos) {
			return null;	// always null
		}

		@Override
		public void set(int ruleIndex, int srcPos, Object value, int currentPos) {
		}
	}

	protected static class ArrayBasedCache implements ResultCache {
		private final CacheEntry[][] resultArray;

		public ArrayBasedCache(int ruleSize, int srcSize) {
			int actualSrcSize = srcSize + 1;
			this.resultArray = new CacheEntry[ruleSize][actualSrcSize];
			for(int i = 0; i < ruleSize; i++) {
				for(int j = 0; j < actualSrcSize; j++) {
					this.resultArray[i][j] = null;
				}
			}
		}

		@Override
		public CacheEntry get(int ruleIndex, int srcPos) {
			return this.resultArray[ruleIndex][srcPos];
		}

		@Override
		public void set(int ruleIndex, int srcPos, Object value, int currentPos) {
			assert(this.resultArray[ruleIndex][srcPos] != null);
			this.resultArray[ruleIndex][srcPos] = CacheEntry.newEntry(currentPos, value);
		}
	}

	protected static class MapBasedCache 
	extends HashMap<Long, CacheEntry>implements ResultCache {
		private static final long serialVersionUID = 7917935601725351378L;

		public MapBasedCache(int cap) {
			super(cap);
		}

		@Override
		public CacheEntry get(int ruleIndex, int srcPos) {
			return this.get(toUniqueId(ruleIndex, srcPos));
		}

		@Override
		public void set(int ruleIndex, int srcPos, Object value, int currentPos) {
			this.put(toUniqueId(ruleIndex, srcPos), CacheEntry.newEntry(currentPos, value));
		}

		protected final static long toUniqueId(int ruleIndex, int srcPos) {
			return ((long)ruleIndex) << 32 | srcPos;
		}
	}

	protected static class LimitedSizeCache implements ResultCache {
		private EntryRow[] entryTable;

		public LimitedSizeCache(int ruleSize, int srcSize) {
			this.entryTable = new EntryRow[ruleSize];
			for(int i = 0; i < ruleSize; i++) {
				this.entryTable[i] = new EntryRow();
			}
		}

		@Override
		public CacheEntry get(int ruleIndex, int srcPos) {
			return this.entryTable[ruleIndex].get(srcPos);
		}

		@Override
		public void set(int ruleIndex, int srcPos, Object value, int currentPos) {
			this.entryTable[ruleIndex].set(srcPos, CacheEntry.newEntry(currentPos, value));
		}
	}

	private static class EntryRow {
		private final static int DEFAULT_ROW_SIZE = 16;
		private CacheEntry[] entries;
		private int[] srcIndexEntries;

		public EntryRow() {
			this.entries = new CacheEntry[DEFAULT_ROW_SIZE];
			this.srcIndexEntries = new int[DEFAULT_ROW_SIZE];
			for(int i = 0; i < DEFAULT_ROW_SIZE; i++) {
				this.entries[i] = null;
				this.srcIndexEntries[i] = -1;
			}
		}

		public void set(int srcIndex, CacheEntry entry) {
			int index = srcIndex % DEFAULT_ROW_SIZE;
			this.srcIndexEntries[index] = srcIndex;
			this.entries[index] = entry;
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
