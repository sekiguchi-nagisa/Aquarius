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

/**
 * for memoization
 * @author skgchxngsxyz-opensuse
 *
 */
public abstract class ResultCache {
	/**
	 * look up previous parsed result
	 * @param ruleIndex
	 * non negative value
	 * @param srcPos
	 * non negative value
	 * @return
	 * return null if not found parsed result
	 */
	public abstract CacheEntry get(int ruleIndex, int srcPos);

	/**
	 * set parsed result.
	 * @param ruleIndex
	 * non negative value
	 * @param srcPos
	 * no negative value
	 * @param value
	 * parsed result
	 * @param currentPos
	 */
	public abstract void set(int ruleIndex, int srcPos, Object value, int currentPos);

	public abstract void setFailure(int ruleIndex, int srcPos);
}
