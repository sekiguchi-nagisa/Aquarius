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
 *//*
 * Copyright (C) 2015 Nagisa Sekiguchi
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


package aquarius.example;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import aquarius.misc.Tuple2;

public interface JSON {
	public static JSONNull NULL = new JSONNull();

	public static class JSONObject extends LinkedHashMap<JSONString, JSON> implements JSON {
		private static final long serialVersionUID = 4227513440823211948L;

		public void add(Tuple2<JSONString, JSON> entry) {
			this.put(entry.get0(), entry.get1());
		}
	}

	public static class JSONArray extends ArrayList<JSON> implements JSON {
		private static final long serialVersionUID = -8956079238990577189L;
	}

	public static class JSONString implements JSON {
		private final String value;

		public JSONString(String value) {
			this.value = stringify(value);
		}

		private static String stringify(String value) {	//TODO: unicode escape
			StringBuilder sBuilder = new StringBuilder();
			final int size = value.length();
			for(int i = 0; i < size; i++) {
				char ch = value.charAt(i);
				if(ch == '\\') {
					char nextCh = value.charAt(++i);
					switch(nextCh) {
					case 'b': ch = '\b'; break;
					case 'f': ch = '\f'; break;
					case 'n': ch = '\n'; break;
					case 'r': ch = '\r'; break;
					case 't': ch = '\t'; break;
					case '\\': ch = nextCh; break;
					case '/': ch = nextCh; break;
					case '"': ch = nextCh; break;
					}
				} else if(ch == '"') {
					continue;
				}
				sBuilder.append(ch);
			}
			return sBuilder.toString();
		}

		public String getValue() {
			return this.value;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			JSONString other = (JSONString) obj;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}
	}

	public static class JSONNumber implements JSON {
		private final Number value;

		public JSONNumber(long value) {
			this.value = value;
		}

		public JSONNumber(double value) {
			this.value = value;
		}

		public Number getValue() {
			return this.value;
		}

		public int intValue() {
			return this.value.intValue();
		}

		public long longValue() {
			return this.value.longValue();
		}

		public float floatValue() {
			return this.value.floatValue();
		}

		public double doubleValue() {
			return this.value.doubleValue();
		}
	}

	public static class JSONBool implements JSON {
		private final boolean value;

		public JSONBool(boolean value) {
			this.value = value;
		}

		public boolean getValue() {
			return this.value;
		}
	}

	public static class JSONNull implements JSON {
	}
}