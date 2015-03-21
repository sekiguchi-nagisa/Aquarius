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