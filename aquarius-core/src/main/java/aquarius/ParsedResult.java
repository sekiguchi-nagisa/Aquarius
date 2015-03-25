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

public class ParsedResult<R> {
	/**
	 * may be null if has no constructed value.
	 */
	private final Object value;

	ParsedResult(Object value) {
		this.value = value;
	}

	public final boolean isSucess() {
		return !this.isFailure();
	}

	public boolean isFailure() {
		return this.value instanceof Failure;
	}

	/**
	 * 
	 * @return
	 * return null if parsing success
	 */
	public Failure getFailure() {
		return this.isFailure() ? (Failure) this.value : null;
	}

	/**
	 * 
	 * @return
	 * may be null if has no constructed value.
	 */
	@SuppressWarnings("unchecked")
	public R getValue() {
		return this.value != null && this.isSucess() ? (R) this.value : null;
	}
}
