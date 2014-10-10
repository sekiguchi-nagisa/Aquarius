package aquarius.runtime.memo;

import java.util.HashMap;

import aquarius.runtime.ParsedResult;

public class MapBasedMemoTableFactory implements MemoTableFactory {
	@Override
	public MemoTable newMemoTable(int ruleSize, int srcSize) {
		return new MapBasedMemoTable(16);
	}

	private static class MapBasedMemoTable 
	extends HashMap<Long, MemoTable.MemoEntry> implements MemoTable {
		private static final long serialVersionUID = 7917935601725351378L;

		private MapBasedMemoTable(int cap) {
			super(cap);
		}

		@Override
		public MemoEntry get(int ruleIndex, int srcPos) {
			return this.get(toUniqueId(ruleIndex, srcPos));
		}

		@Override
		public ParsedResult set(int ruleIndex, int srcPos, ParsedResult result, int currentPos) {
			this.put(toUniqueId(ruleIndex, srcPos), new MemoEntry(currentPos, result));
			return result;
		}

		protected final static long toUniqueId(int ruleIndex, int srcPos) {
			return ((long)ruleIndex) << 32 | srcPos;
		}
	}
}
