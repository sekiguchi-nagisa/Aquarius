/*
 * Copyright (C) 2014-2015 Nagisa Sekiguchi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package aquarius;

public class CacheFactory {
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
        return new LimitedSizeCache(ruleSize);
    }

    public static enum CacheKind {
        Empty, Limit;
    }

    /**
     * not use memoization
     *
     * @author skgchxngsxyz-opensuse
     */
    protected static class EmptyCache extends ResultCache {
        @Override
        public CacheEntry get(int ruleIndex, int srcPos) {
            return null;    // always null
        }

        @Override
        public void set(int ruleIndex, int srcPos, Object value, int currentPos) {
        }

        @Override
        public void setFailure(int ruleIndex, int srcPos) {
        }
    }

    private static class LimitedSizeCache extends ResultCache {
        private final static int rowSize = 16;

        private final int tableSize;
        private final Entry[] entries;

        public LimitedSizeCache(int ruleSize) {
            this.tableSize = rowSize * ruleSize;
            this.entries = new Entry[this.tableSize];
            for(int i = 0; i < this.tableSize; i++) {
                this.entries[i] = new Entry();
            }
        }

        private static int toIndex(int ruleIndex, int srcPos) {
            return (srcPos % rowSize) + (ruleIndex * rowSize);
        }

        @Override
        public CacheEntry get(int ruleIndex, int srcPos) {
            Entry e = this.entries[toIndex(ruleIndex, srcPos)];
            return e.srcPos == srcPos ? e.entry : null;
        }

        @Override
        public void set(int ruleIndex, int srcPos, Object value, int currentPos) {
            Entry e = this.entries[toIndex(ruleIndex, srcPos)];
            e.srcPos = srcPos;
            e.entry.reuse(currentPos, value);
        }

        @Override
        public void setFailure(int ruleIndex, int srcPos) {
            Entry e = this.entries[toIndex(ruleIndex, srcPos)];
            e.srcPos = srcPos;
            e.entry.reuse(-1, null);
        }
    }

    private static class Entry {
        public int srcPos = -1;
        public CacheEntry entry = new CacheEntry();
    }
}
