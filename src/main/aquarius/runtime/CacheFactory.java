package aquarius.runtime;

import java.util.HashMap;

public class CacheFactory {
	public static enum CacheKind {
		Empty, Array, Map;
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
		}
		return null;
	}

	protected static class EmptyCache implements ResultCache {
		@Override
		public CacheEntry get(int ruleIndex, int srcPos) {
			return null;	// always null
		}

		@Override
		public <R> Result<R> set(int ruleIndex, int srcPos, Result<R> result, int currentPos) {
			return result;
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
		public <R> Result<R> set(int ruleIndex, int srcPos, Result<R> result, int currentPos) {
			assert(this.resultArray[ruleIndex][srcPos] != null);
			assert(!(result.isFailure()));

			this.resultArray[ruleIndex][srcPos] = new CacheEntry(currentPos, result);
			return result;
		}
	}

	protected static class MapBasedCache 
	extends HashMap<Long, ResultCache.CacheEntry>implements ResultCache {
		private static final long serialVersionUID = 7917935601725351378L;

		public MapBasedCache(int cap) {
			super(cap);
		}

		@Override
		public CacheEntry get(int ruleIndex, int srcPos) {
			return this.get(toUniqueId(ruleIndex, srcPos));
		}

		@Override
		public <R> Result<R> set(int ruleIndex, int srcPos, Result<R> result, int currentPos) {
			this.put(toUniqueId(ruleIndex, srcPos), new CacheEntry(currentPos, result));
			return result;
		}

		protected final static long toUniqueId(int ruleIndex, int srcPos) {
			return ((long)ruleIndex) << 32 | srcPos;
		}
	}
}
